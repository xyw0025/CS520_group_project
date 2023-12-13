package com.group.cs520;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;

import com.group.cs520.repository.MatchRepository;
import com.group.cs520.repository.UserRepository;
import com.group.cs520.model.Match;
import com.group.cs520.model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        // Clear repositories before each test
        matchRepository.deleteAll();
        userRepository.deleteAll();

        // Create and save test users
        User user1 = new User(); // Set user properties
        User user2 = new User(); // Set user properties
        userRepository.save(user1);
        userRepository.save(user2);
        
        List<ObjectId> users = Arrays.asList(user1.getId(), user2.getId());
        // Create and save test matches
        Match match = new Match(users); // Set match properties
        // Set any properties related to the match
        matchRepository.save(match);
    }

    @AfterEach
    public void cleanup() {
        // Clear repositories after each test
        matchRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testGetAllMatches() throws Exception {
        mockMvc.perform(get("/api/v1/match"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetAllSuccessMatches() throws Exception {
        mockMvc.perform(get("/api/v1/match/matched"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
                // Additional assertions...
    }

    @Test
    public void testGetMatchByUserIds() throws Exception {
        List<ObjectId> userIds = matchRepository.findAll().getFirst().getUserIds();

        mockMvc.perform(get("/api/v1/match/userIds")
                .param("userId1", userIds.get(0).toString())
                .param("userId2", userIds.get(1).toString()))
                .andExpect(status().isOk());
                // Additional assertions...
    }

    @Test
    public void testUpdateMatchHistory() throws Exception {
        List<ObjectId> userIds = matchRepository.findAll().getFirst().getUserIds();
        String payload = String.format("{\"senderId\":\"%s\", \"receiverId\":\"%s\", \"behavior\":\"ACCEPT\"}", userIds.get(0).toString(), userIds.get(1).toString());

        mockMvc.perform(post("/api/v1/match/add-match-history")
                .contentType(APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
                // Additional assertions...
    }

    @Test
    public void testGetMatchedUsers() throws Exception {
        List<ObjectId> userIds = matchRepository.findAll().getFirst().getUserIds();
        String userId = userIds.get(0).toString(); // Replace with valid user ID

        mockMvc.perform(get("/api/v1/match/get-all-matched-users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

}
