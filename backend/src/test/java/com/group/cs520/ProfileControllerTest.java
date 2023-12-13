package com.group.cs520;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.group.cs520.repository.ProfileRepository;
import com.group.cs520.repository.UserRepository;
import com.group.cs520.model.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        // Clear repositories to start fresh
        profileRepository.deleteAll();
        userRepository.deleteAll();

        // Create and save a sample user
        User user = new User();
        user.setEmail("sampleUser@example.com");
        user.setPassword("samplePassword"); // Ensure this is hashed if your User entity expects a hashed password

        // Create and save a sample profile
        Profile profile = new Profile();
        profile.setDisplayName("Sample User");
        profileRepository.save(profile);
        user.setProfile(profile);
        userRepository.save(user);
        // Set other fields of the profile as necessary
        
    }

    @AfterEach
    public void cleanup() {
        // Clears the database
        userRepository.deleteAll(); 
        profileRepository.deleteAll();
    }


    @Test
    public void testGetAllProfiles() throws Exception {
        mockMvc.perform(get("/api/v1/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        User user = userRepository.findAll().getFirst();
        System.out.print("....????");
        System.out.print(user.getId().toString());
        String profileJson = "{\"displayName\": \"yoyoyo\", \"gender\": \"male\", \"birthday\": \"1999-11-01\", \"preferences\": []}"; // replace with valid payload

        mockMvc.perform(put("/api/v1/profile/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(profileJson))
                .andExpect(status().isOk());
    }
}
