package com.bdmtr.slotman.controller;

import com.bdmtr.slotman.model.dto.UserDTO;
import com.bdmtr.slotman.model.entity.Status;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The `UserManagementController` class is a controller responsible for managing user-related operations and endpoints.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:8081")
public class UserManagementController {

    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles the HTTP GET request to retrieve all users.
     *
     * @return A `ResponseEntity` containing a list of users and an HTTP status code (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Handles the HTTP GET request to retrieve a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A `ResponseEntity` containing the user's details and an HTTP status code (OK) if found, or NOT_FOUND if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Handles the HTTP POST request to create a new user.
     *
     * @param user The user details to be created.
     * @return A `ResponseEntity` with an HTTP status code (CREATED) indicating a successful user creation.
     */
    @PostMapping("/create")
    public ResponseEntity<User> addUser(@RequestBody UserDTO user) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Handles the HTTP POST request to update an existing user.
     *
     * @param user The updated user details.
     * @return A `ResponseEntity` with an HTTP status code (OK) indicating a successful user update.
     */
    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Handles the HTTP POST request to update the status of a user by their ID.
     *
     * @param id     The ID of the user to update.
     * @param status The new status for the user.
     * @return A `ResponseEntity` with an HTTP status code (OK) indicating a successful user status update.
     */
    @PostMapping("/{id}/update")
    public ResponseEntity<User> updateUserStatusById(@PathVariable("id") Integer id,
                                                     @RequestParam("status") Status status) {
        userService.updateUserStatusById(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}