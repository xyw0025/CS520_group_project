package com.group.cs520.documentation;
import com.group.cs520.model.Profile;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


import java.util.List;
import java.util.Map;

@Tag(name = "Profile API")
public interface ProfileApi {

    @Operation(summary = "Retrieves all profiles")
    ResponseEntity<List<Profile>> getAllProfile();

    @Operation(summary = "Retrieves a profile by user ID")
    ResponseEntity<Profile> getProfileByUser(String userId);

    @Operation(summary = "Retrieves a single profile by ID")
    ResponseEntity<Profile> getSingleProfile(ObjectId id);

    @Operation(summary = "Updates profile preferences")
    ResponseEntity<?> updateProfilePreferences(String profileId, Map<String, String> payload);

    @Operation(summary = "Updates a profile")
    ResponseEntity<?> updateProfile(ObjectId id, @RequestBody(description = "Preference payload", required = true, 
            content = @Content(schema = @Schema(implementation = UpdateProfilePayload.class))
        ) Map<String, String> payload);


    class UpdateProfilePayload {
        @Schema(description = "Display Name", example = "Jane Doe")
        public String displayName;
    
        @Schema(description = "Gender -> **** prolly should change to string type . Let backend handle the enum mapping logic", example = "0")
        public int gender;
    
        @Schema(description = "Birthday", example = "1922-01-01")
        public String birthday;
    
        @Schema(description = "Age", example = "0")
        public int age;
    
        @Schema(description = "Bio", example = "Loves hiking and outdoor activities.")
        public String bio;
    
        @Schema(description = "User ID", example = "655d3d90fee7474f900da830")
        public String userId;
    
        @Schema(description = "Image URLs", example = "[\"http://example.com/profile1.jpg\", \"http://example.com/profile2.jpg\"]")
        public List<String> imageUrls;    
    }
}
