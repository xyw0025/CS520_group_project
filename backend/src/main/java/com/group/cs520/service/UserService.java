package com.group.cs520.service;

import com.group.cs520.model.User;
import com.group.cs520.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public Optional<User> singleUser(ObjectId id) {
        return userRepository.findById(id);
    }

    public User createUser(String email, String password) {
        System.out.println("start check user information");
        //check email first
        Optional<User> existingUser = userRepository.findUserByEmail(email);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already in use.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword);
        user.setIsActive(true);
        user.setCreatedTime(Instant.now());
        user.setUpdatedTime(Instant.now());

        // for dev
        User newUser = userRepository.insert(user);
        System.out.println("User created successfully: " + newUser);

        return newUser;
    }

    public List<User> activeUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public Optional<User> singleUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
