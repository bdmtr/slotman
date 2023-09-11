package com.bdmtr.slotman.model.service;

import com.bdmtr.slotman.exception.UserExistException;
import com.bdmtr.slotman.exception.UserNotFoundException;
import com.bdmtr.slotman.model.dto.UserDTO;
import com.bdmtr.slotman.model.entity.Role;
import com.bdmtr.slotman.model.entity.Status;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.repository.RoleRepository;
import com.bdmtr.slotman.model.repository.StatusRepository;
import com.bdmtr.slotman.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, StatusRepository statusRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer userId) {
        return Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Cant find user: " + userId)));
    }

    @Transactional
    public void createUser(UserDTO userDTO) {
        String username = userDTO.getUsername();
        Optional<User> newUser = userRepository.findByUsername(username);
        if (newUser.isPresent()) {
            throw new UserExistException("User already exists");
        }
        String password = passwordEncoder.encode(userDTO.getPassword());
        String userName = userDTO.getUsername();
        int income = 0;
        int outcome = 0;
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        Role userRole = roleRepository.findByName("USER");
        Status userStatus = statusRepository.findByName("active");

        User user = new User(userName, password, income, outcome, timestamp, userRole, userStatus);

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User updatedUser) {
        Integer userId = updatedUser.getId();
        Optional<User> existingUserOptional = Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Cant find user: " + userId)));
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setRole(updatedUser.getRole());
            existingUser.setStatus(updatedUser.getStatus());
        }
    }

    @Transactional
    public void updateUserStatusById(Integer userId, Status newStatus) {
        Optional<User> existingUserOptional = Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Cant find user: " + userId)));

        if (existingUserOptional.isPresent()) {
            userRepository.updateUserStatusById(userId, newStatus);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}