package com.bdmtr.slotman.model.service;

import com.bdmtr.slotman.model.entity.Role;
import com.bdmtr.slotman.model.entity.Status;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.repository.RoleRepository;
import com.bdmtr.slotman.model.repository.StatusRepository;
import com.bdmtr.slotman.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, StatusRepository statusRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    public void createUser(User user) {
        String username = user.getUsername();
        Optional<User> newUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (newUser.isEmpty()) {
            user.setIncome(0);
            user.setOutcome(0);
            Role userRole = roleRepository.findByName("user");
            Status userStatus = statusRepository.findByName("active");
            user.setRole(userRole);
            user.setStatus(userStatus);
            user.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
            userRepository.save(user);
        }
    }

    public void updateUser(User updatedUser) {
        Integer userId = updatedUser.getId();
        Optional<User> existingUserOptional = userRepository.findById(userId);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setStatus(updatedUser.getStatus());
        }
    }

    public void updateUserStatusById(Integer userId, Status newStatus) {
        Optional<User> existingUserOptional = userRepository.findById(userId);

        if (existingUserOptional.isPresent()) {
            userRepository.updateUserStatusById(userId, newStatus);
        }
    }
}