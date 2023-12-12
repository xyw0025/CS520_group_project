package com.group.cs520.service;

import com.group.cs520.model.Profile;
import com.group.cs520.model.User;
import com.group.cs520.model.Match;
import com.group.cs520.repository.MatchRepository;
import com.group.cs520.repository.UserRepository;
import com.group.cs520.repository.ProfileRepository;
import com.group.cs520.service.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import com.group.cs520.service.TypeUtil;


@Service
public class UserService {

    private final JwtUtil jwtUtil;
    public UserService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;


    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User singleUser(String id) {
        ObjectId userId = TypeUtil.objectIdConverter(id);
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
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
        System.out.println("start checking user's information");
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
        Profile profile = new Profile();
        profileRepository.save(profile);
        user.setProfile(profile);
        return userRepository.save(user);
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

    public List<User> getRandomUsers(int limit, String userId) {
        // get other users that has no match record with the user

        List<Match> matches = matchRepository.findByUserIdsContains(TypeUtil.objectIdConverter(userId));

        List<ObjectId> visitedUserIds = matches.stream()
                // Filter matches based on the new conditions
                .filter(match -> match.getStatus() == 1 ||
                        (match.getStatus() == 0 && !match.getMatchHistories().isEmpty() &&
                                match.getMatchHistories().get(0).getSenderId().equals(TypeUtil.objectIdConverter(userId))))
                // Extract userIds from the filtered matches
                .flatMap(match -> match.getUserIds().stream())
                .distinct()
                .collect(Collectors.toList());

        visitedUserIds.add(TypeUtil.objectIdConverter(userId));

        // Step 2: Find users who are not in visitedUserIds
        List<User> users = userRepository.findByIdNotIn(visitedUserIds, PageRequest.of(0, limit));
        return users;
    }

//    public List<User> getRandomUsers(int limit) {
//        return userRepository.findRandomUsers(limit);
//    }

//    public List<User> getFirstFiveUsers() {
//        return userRepository.findAll().stream().limit(5).collect(Collectors.toList());
//    }

    public Optional<User> singleUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void setProfile(String userId, Profile profile) {
        User user = this.singleUser(userId);
        user.setProfile(profile);
        mongoTemplate.save(user);
    }

    public void addMatch(String user_id, Match match) {
        User user = this.singleUser(user_id);
        List<Match> matches = user.getMatches();
        matches.add(match);
        user.setMatches(matches);
        userRepository.save(user);
    }

    public List<Match> userMatches(String user_id) {
        User user = this.singleUser(user_id);
        List<Match> filteredMatches = user.getMatches().stream()
                                      .filter(match -> match.getStatus() == 1)
                                      .collect(Collectors.toList());

        return filteredMatches;
    }
}
