package com.group.cs520.documentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;



import com.group.cs520.model.Preference;

import java.util.List;
import java.util.Map;

@Tag(name = "Preference API")
public interface PreferenceApi {

    @Operation(summary = "Get all preferences")
    ResponseEntity<List<Preference>> getAllPreferences();

    @Operation(summary = "Create a preference")
    ResponseEntity<?> createPreference(@RequestBody(description = "Preference payload", required = true, 
            content = @Content(schema = @Schema(implementation = PreferencePayload.class))
        ) Map<String, String> payload);


    class PreferencePayload {
        @Schema(description = "Display Name", example = "John Doe")
        public String displayName;

        @Schema(description = "Gender", example = "1")
        public int gender;

        @Schema(description = "Birthday", example = "1990-01-01")
        public String birthday;

        @Schema(description = "Age", example = "30")
        public int age;

        @Schema(description = "Bio", example = "Enthusiastic traveler and food lover.")
        public String bio;

        @Schema(description = "User ID", example = "655d3d90fee7474f900da830")
        public String userId;

        @Schema(description = "Image URLs", example = "[\"http://example.com/image1.jpg\", \"http://example.com/image2.jpg\"]")
        public List<String> imageUrls;
    }
}

        