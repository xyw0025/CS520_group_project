package com.group.cs520.documentation;
import com.group.cs520.model.Profile;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.Map;

@Tag(name = "Profile API")
public interface ProfileApi {
    @Operation(summary = "Retrieves all profiles")
    @ApiResponse(responseCode = "200", description = "OK", 
        content = {
            @Content(mediaType = "application/json", 
                examples = {        
                    @ExampleObject(
                        name = "exampleProfilesListMock",
                        value = "["
                            + "{"
                            + "\"id\": \"a1b2c3d4e5f67890abcd1234\","
                            + "\"displayName\": \"AlexSample\","
                            + "\"gender\": 1,"
                            + "\"birthday\": \"1988-04-12\","
                            + "\"age\": 35,"
                            + "\"imageUrls\": ["
                            + "    \"https://example.com/images/profile123.jpg\""
                            + "],"
                            + "\"bio\": \"Avid reader and coffee enthusiast.\","
                            + "\"isDeleted\": false,"
                            + "\"createdTime\": \"2023-10-02T10:15:30.123Z\","
                            + "\"updatedTime\": \"2023-10-02T10:15:30.123Z\","
                            + "\"preferences\": ["
                            + "    {"
                            + "        \"id\": \"p1a2b3c4d5e6f7g8h9i0j1k2\","
                            + "        \"categoryId\": 4,"
                            + "        \"name\": \"literature\","
                            + "        \"createdTime\": \"2023-10-01T07:00:00.000Z\","
                            + "        \"updatedTime\": \"2023-10-10T08:00:00.000Z\""
                            + "    },"
                            + "    {"
                            + "        \"id\": \"l1k2j3i4h5g6f7e8d9c0b1a2\","
                            + "        \"categoryId\": 7,"
                            + "        \"name\": \"gourmet\","
                            + "        \"createdTime\": \"2023-10-01T07:15:00.000Z\","
                            + "        \"updatedTime\": null"
                            + "    }"
                            + "]"
                            + "},"
                            + "{"
                            + "\"id\": \"b2c3d4e5f67890abcd1234a1\","
                            + "\"displayName\": \"SamRandom\","
                            + "\"gender\": 2,"
                            + "\"birthday\": \"1992-11-20\","
                            + "\"age\": 31,"
                            + "\"imageUrls\": null,"
                            + "\"bio\": \"Tech enthusiast and blogger.\","
                            + "\"isDeleted\": false,"
                            + "\"createdTime\": \"2023-09-15T14:35:45.678Z\","
                            + "\"updatedTime\": \"2023-09-20T19:45:55.789Z\","
                            + "\"preferences\": []"
                            + "}"
                            + "]"
                    )
                })
        })            
    ResponseEntity<List<Profile>> getAllProfile();

    @Operation(summary = "Retrieves a profile by user ID")
    ResponseEntity<Profile> getProfileByUser(@Parameter(description = "user id") String userId);

    @Operation(summary = "Retrieves a single profile by ID")
    @ApiResponse(responseCode = "200", description = "OK", 
    content = {
        @Content(mediaType = "application/json", 
            examples = {
                @ExampleObject(
                    name = "exampleProfileMock",
                    value = "{"
                        + "\"id\": \"623b3f24ef45bd7f85e18a67\","
                        + "\"displayName\": \"JaneDoeSample\","
                        + "\"gender\": 1,"
                        + "\"birthday\": \"1990-05-15\","
                        + "\"age\": 33,"
                        + "\"imageUrls\": ["
                        + "    \"https://example.com/images/profile1.jpg\""
                        + "],"
                        + "\"bio\": \"Loves hiking and outdoor photography.\","
                        + "\"isDeleted\": false,"
                        + "\"createdTime\": \"2023-10-05T14:21:33.988Z\","
                        + "\"updatedTime\": \"2023-11-15T09:30:27.456Z\","
                        + "\"preferences\": ["
                        + "    {"
                        + "        \"id\": \"678fde3422e58b53b18a12f4\","
                        + "        \"categoryId\": 5,"
                        + "        \"name\": \"travel\","
                        + "        \"createdTime\": \"2023-10-10T08:00:10.254Z\","
                        + "        \"updatedTime\": \"2023-11-01T11:22:33.030Z\""
                        + "    },"
                        + "    {"
                        + "        \"id\": \"679ade32a3c59d64d18b6897\","
                        + "        \"categoryId\": 3,"
                        + "        \"name\": \"cooking\","
                        + "        \"createdTime\": \"2023-10-15T17:45:00.030Z\","
                        + "        \"updatedTime\": \"2023-11-02T12:31:45.456Z\""
                        + "    }"
                        + "]"
                        + "}"
                )
            })
    })
    ResponseEntity<Profile> getSingleProfile(@Parameter(description = "profile id", in = ParameterIn.PATH) @PathVariable(value = "id") String id);

    @Operation(summary = "Updates a profile")
    @ApiResponse(responseCode = "200", description = "OK", 
        content = {
            @Content(mediaType = "application/json", 
                examples = {
                    @ExampleObject(
                        name = "exampleProfileMock",
                        value = "{"
                            + "\"id\": \"623b3f24ef45bd7f85e18a67\","
                            + "\"displayName\": \"JaneDoeSample\","
                            + "\"gender\": 1,"
                            + "\"birthday\": \"1990-05-15\","
                            + "\"age\": 33,"
                            + "\"imageUrls\": ["
                            + "    \"https://example.com/images/profile1.jpg\""
                            + "],"
                            + "\"bio\": \"Loves hiking and outdoor photography.\","
                            + "\"isDeleted\": false,"
                            + "\"createdTime\": \"2023-10-05T14:21:33.988Z\","
                            + "\"updatedTime\": \"2023-11-15T09:30:27.456Z\","
                            + "\"preferences\": ["
                            + "    {"
                            + "        \"id\": \"678fde3422e58b53b18a12f4\","
                            + "        \"categoryId\": 5,"
                            + "        \"name\": \"travel\","
                            + "        \"createdTime\": \"2023-10-10T08:00:10.254Z\","
                            + "        \"updatedTime\": \"2023-11-01T11:22:33.030Z\""
                            + "    },"
                            + "    {"
                            + "        \"id\": \"679ade32a3c59d64d18b6897\","
                            + "        \"categoryId\": 3,"
                            + "        \"name\": \"cooking\","
                            + "        \"createdTime\": \"2023-10-15T17:45:00.030Z\","
                            + "        \"updatedTime\": \"2023-11-02T12:31:45.456Z\""
                            + "    }"
                            + "]"
                            + "}"
                    )
                })
        })
    ResponseEntity<?> updateProfile(@Parameter(description = "user id") @PathVariable(value = "user id") String id, @RequestBody(description = "profile payload", required = true, 
            content = @Content(schema = @Schema(implementation = UpdateProfilePayload.class))
        ) Map<String, Object> payload);

    class UpdateProfilePayload {
        @Schema(description = "Display Name", example = "Jane Doe")
        public String displayName;
    
        @Schema(description = "Gender", example = "Female")
        public String gender;
    
        @Schema(description = "Birthday", example = "1922-01-01")
        public String birthday;
    
        @Schema(description = "Bio", example = "Loves hiking and outdoor activities.")
        public String bio;
    
        @Schema(description = "Image URLs", example = "[\"http://example.com/profile1.jpg\", \"http://example.com/profile2.jpg\"]")
        public List<String> imageUrls;    

        @Schema(description = "Preference",  example ="[\"food\", \"baking\"]")
        public List<String> preferences;
    }
}
