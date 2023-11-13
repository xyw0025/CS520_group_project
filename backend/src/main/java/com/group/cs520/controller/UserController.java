package com.group.cs520.controller;

import com.group.cs520.model.User;
import com.group.cs520.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> activeUsers = userService.activeUsers();
        return ResponseEntity.ok(activeUsers);
    }

    // TODO: handle different filter
    @GetMapping("/search")
    public ResponseEntity<User> getSingleUserByParam(@RequestParam(name = "email") String email) {
        User user = userService.singleUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> payload) {
        try {
            User user = userService.createUser(payload.get("email"), payload.get("password"));
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            // Handle the case where email is already in use
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            Map<String, Object> authInfo = userService.authenticateUser(email, password);
            String token = (String) authInfo.get("token");
            User user = (User) authInfo.get("user");

            Cookie cookie = new Cookie("authorization", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            // TODO: return suitable user infomation by JsonIgnore in model
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("authorization", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Successfully logged out"));
    }

//    TODO: Spring Security
//    @GetMapping("/current")
//    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
//        String token = (String) authentication.getCredentials();
//        System.out.println(token);
//        User user = userService.validateUser(token);
//        return ResponseEntity.ok(user);
//    }
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 或其他适当的响应
        }
        User user = userService.validateUser(token);
        return ResponseEntity.ok(user);
    }

    // find single user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getSingleUser(@PathVariable ObjectId id) {
        User user = userService.singleUser(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(user);
    }


}
