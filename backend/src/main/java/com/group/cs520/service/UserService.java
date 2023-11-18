package com.group.cs520.service;

import com.group.cs520.model.User;
import com.group.cs520.repository.UserRepository;
import com.group.cs520.service.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final JwtUtil jwtUtil;
    public UserService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

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

    /**
     * Creates a new user with the given email and password.
     * 
     * @param email    the email of the user
     * @param password the password of the user
     * @return the created user
     * @throws IllegalArgumentException if the email is already in use
     */
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
        return userRepository.insert(user);
    }

    /**
     * Authenticates a user by checking the provided email and password.
     * If the credentials are valid, a token is generated
     * 
     * @param email  
     * @param password
     * @return A map containing {user information: authentication token}
     * @throws IllegalArgumentException If the credentials are invalid.
     */
    public Map<String, Object> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findUserByEmail(email);

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            String token = jwtUtil.createToken(email);
            Map<String, Object> authInfo = new HashMap<>();
            authInfo.put("user", userOpt.get());
            authInfo.put("token", token);
            return authInfo;
        } else {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    /**
     * Validates a user based on the provided token.
     * 
     * @param token 
     * @return the validated User object
     * @throws IllegalArgumentException if the user is not found or the JWT token is invalid
     */
    public User validateUser(String token) {
        try {
            String email = jwtUtil.extractUserId(token);
            return userRepository.findUserByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
    }

    public List<User> activeUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public Optional<User> singleUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
