package com.group.cs520;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.group.cs520.service.UserService;

import com.group.cs520.repository.UserRepository;
import com.group.cs520.model.User;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import org.springframework.http.MediaType;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        User testUser = new User("existinguser@example.com", "asgasdg");
        User activeUser2 = new User("456@email.com", "JaneDoe");
        userRepository.save(testUser);
        userRepository.save(activeUser2);
    }


    @AfterEach
    public void cleanup() {
        userRepository.deleteAll(); // Clears the database
    }

    @Test
    public void testGetFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/users")) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    public void testCreateUser_Success() throws Exception {
        String userJson = "{\"email\":\"newuser@example.com\",\"password\":\"password123\"}";

        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("newuser@example.com"));
    }


    @Test
    public void testCreateUser_Conflict() throws Exception {
        String userJson = "{\"email\":\"existinguser@example.com\",\"password\":\"password123\"}";

        // Assuming "existinguser@example.com" is already in the database
        mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
                // Check the specific error message if needed
    }
}
