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
import com.bdmtr.slotman.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class for user-related operations, including user creation, retrieval, and updates.
 *
 * @see UserRepository
 * @see RoleRepository
 * @see StatusRepository
 * @see PasswordEncoder
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new UserService instance.
     *
     * @param userRepository   The repository for user data.
     * @param roleRepository   The repository for role data.
     * @param statusRepository The repository for status data.
     * @param passwordEncoder  The password encoder for securing user passwords.
     */
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, StatusRepository statusRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return A list of User objects representing all users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId The unique ID of the user to retrieve.
     * @return An Optional containing the User object if found, or empty if not found.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    public Optional<User> getUserById(Integer userId) {
        return Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Cant find user: " + userId)));
    }

    /**
     * Creates a new user based on the provided userDTO.
     *
     * @param userDTO The UserDTO object containing user details for registration.
     * @throws UserExistException If a user with the same username already exists.
     */
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

    /**
     * Updates the information of an existing user based on the provided User object.
     *
     * @param updatedUser The User object containing updated user information.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
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

    /**
     * Updates the status of an existing user identified by their unique ID.
     *
     * @param userId    The unique ID of the user whose status is to be updated.
     * @param newStatus The new Status object to set for the user.
     * @throws UserNotFoundException If the user with the specified ID is not found.
     */
    @Transactional
    public void updateUserStatusById(Integer userId, Status newStatus) {
        Optional<User> existingUserOptional = Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Cant find user: " + userId)));

        if (existingUserOptional.isPresent()) {
            userRepository.updateUserStatusById(userId, newStatus);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new CustomUserDetails(user);
    }
}