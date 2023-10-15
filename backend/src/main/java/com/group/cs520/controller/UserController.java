package com.group.cs520.controller;


import com.group.cs520.model.User;
import com.group.cs520.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("all")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(userService.allUsers(), HttpStatus.OK);
    }

    // Single item

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getSingleUser(@PathVariable ObjectId id) {
        return new ResponseEntity<>(userService.singleUser(id), HttpStatus.OK);
    }


    // TODO: handle different filter
    @GetMapping
    public ResponseEntity<Optional<User>> getSingleUserByParam(@RequestParam(name = "email") String email) {
        return new ResponseEntity<>(userService.singleUserByEmail(email), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> payload) {
        User user = userService.createUser(payload.get("email"), payload.get("password"));
        if (user != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
