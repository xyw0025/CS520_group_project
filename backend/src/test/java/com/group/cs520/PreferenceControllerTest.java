package com.group.cs520;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PreferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllPreferences() throws Exception {
        mockMvc.perform(get("/api/v1/preference"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
                // Further assertions can be added here
    }
}
