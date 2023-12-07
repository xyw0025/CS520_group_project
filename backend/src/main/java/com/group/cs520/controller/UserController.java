package com.group.cs520.controller;

import com.group.cs520.model.Match;
import com.group.cs520.model.User;
import com.group.cs520.service.UserService;
import com.group.cs520.service.ProfileService;
import com.group.cs520.service.GCPStorageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.group.cs520.documentation.UserApi;
import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserApi {
    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private GCPStorageService gcpStorageService;

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing a list of User objects
     */
    @Override
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves all active users.
     *
     * @return ResponseEntity containing a list of active User objects
     */
    @Override
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> activeUsers = userService.activeUsers();
        return ResponseEntity.ok(activeUsers);
    }

    /**
     * Retrieves a single user by email.
     *
     * @param email the email of the user to retrieve
     * @return ResponseEntity containing the User object
     * @throws ResponseStatusException if the user is not found
     */
    @Override
    @GetMapping("/search")
    public ResponseEntity<User> getSingleUserByParam(@RequestParam(name = "email") String email) {
        User user = userService.singleUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(user);
    }

    /**
     * Creates a new user.
     *
     * @param payload the user data
     * @return ResponseEntity containing the created User object
     */
    @Override
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

    /**
     * Logs in a user using the email and password extracted from cookies
     * If login is sucessful, store JWT token in cookies

     * @param credentials the user's login credentials
     * @param response    the HTTP response object
     * @return ResponseEntity containing the logged-in User object
     */
    @Override
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
            // TODO: return suitable user information by JsonIgnore in model
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Logs out a user: clearing the JWT token from cookies.
     * 
     * 
     * @param response the HTTP response object
     * @return ResponseEntity with a success message
     */
    @Override
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("authorization", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Successfully logged out"));
    }

    /**
     * Retrieves the current user.
     *
     * @param request the HTTP request object
     * @return ResponseEntity containing the current User object
     */
    @Override
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authorization".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token == null) {
            return ResponseEntity.ok(null);
        }
        User user = userService.validateUser(token);
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieves a single user by ID.
     *
     * @param id the ID of the user to retrieve
     * @return ResponseEntity containing the User object
     * @throws ResponseStatusException if the user is not found
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<User> getSingleUser(@PathVariable String id) {
        User user = userService.singleUser(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/random")
    public List<User> suggestRandomMatches(){
        List<User> recommendedUsers = userService.getRandomUsers(5);
        return recommendedUsers;
    }

    @GetMapping("/{id}/fetch-random-5-unmatched")
    public List<User> suggestRandomFiveUnmatchedUsers(@PathVariable String id) {
        List<User> recommendedUsers = userService.getRandomUsers(5, id);
        return recommendedUsers;
    }

    @GetMapping("/{user_id}/match")
    public ResponseEntity<List<Match>> getUserMatches(@PathVariable String id) {
        List<Match> matches = userService.userMatches(id);
        return ResponseEntity.ok(matches);
    }
    
    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) throws IOException {
        String photoURL = gcpStorageService.uploadImage(file);
        profileService.setProfilePhoto(id, photoURL);
        return ResponseEntity.ok(photoURL);
    }
}
