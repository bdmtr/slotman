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

@RestController
@RequestMapping("/api/users")
@CrossOrigin("http://localhost:8081")
public class UserManagementController {

    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<User> addUser(@RequestBody UserDTO user) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<User> updateUserStatusById(@PathVariable("id") Integer id,
                                                     @RequestParam("status") Status status) {
        userService.updateUserStatusById(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}