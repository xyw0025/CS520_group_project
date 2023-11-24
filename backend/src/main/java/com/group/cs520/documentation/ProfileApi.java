package com.group.cs520.documentation;
import com.group.cs520.model.Profile;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


import java.util.List;
import java.util.Map;

@Tag(name = "Profile API")
public interface ProfileApi {

    @Operation(summary = "Retrieves all profiles")
    ResponseEntity<List<Profile>> getAllProfile();

    @Operation(summary = "Retrieves a profile by user ID")
    ResponseEntity<Profile> getProfileByUser(@Parameter(description = "user id") String userId);

    @Operation(summary = "Retrieves a single profile by ID")
    ResponseEntity<Profile> getSingleProfile(@Parameter(description = "profile id") String id);

    @Operation(summary = "Updates a profile")
    ResponseEntity<?> updateProfile(@Parameter(description = "profile id ** or should use user id instead? -> but that'd be not so RESTful.. **") String id, @RequestBody(description = "Preference payload", required = true, 
            content = @Content(schema = @Schema(implementation = UpdateProfilePayload.class))
        ) Map<String, String> payload);

    class UpdateProfilePayload {
        @Schema(description = "Display Name", example = "Jane Doe")
        public String displayName;
    
        @Schema(description = "Gender -> **** prolly should change to string type . Let backend handle the enum mapping logic", example = "0")
        public int gender;
    
        @Schema(description = "Birthday", example = "1922-01-01")
        public String birthday;
    
    
        @Schema(description = "Bio", example = "Loves hiking and outdoor activities.")
        public String bio;
    
        @Schema(description = "Image URLs", example = "[\"http://example.com/profile1.jpg\", \"http://example.com/profile2.jpg\"]")
        public List<String> imageUrls;    

        @Schema(description = "Preference",  example ="[\"655ec8e65e588b53b18d5885\", \"655ec8dc5e588b53b18d5883\"]")
        public List<ObjectId> preferences;
    }
}
