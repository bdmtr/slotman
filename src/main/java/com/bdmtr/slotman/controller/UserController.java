package com.bdmtr.slotman.controller;

import com.bdmtr.slotman.model.entity.Status;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/create")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/{id}/update")
    public ResponseEntity<User> updateUserStatusById(@PathVariable("id") Integer id,
                                                     @RequestParam("status") Status status) {
        userService.updateUserStatusById(id, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}