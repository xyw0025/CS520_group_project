package com.group.cs520.service;

import com.group.cs520.model.User;
import com.group.cs520.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Value("${jwt.secret}")
    private String jwtSecret;
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

    public String authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findUserByEmail(email);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            Date issuedAt = new Date();
            Date expiresAt = new Date(issuedAt.getTime() + 24 * 60 * 60 * 1000); // expire after one days

            return JWT.create()
                    .withSubject(email)
                    .withIssuedAt(issuedAt)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(jwtSecret));
        } else {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    public List<User> activeUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public Optional<User> singleUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
