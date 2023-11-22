package com.group.cs520.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "profiles")
public class Profile {
    @Id
    private ObjectId id;

    @NotBlank
    private String displayName;

    @NotBlank
    private Integer gender;

    @NotBlank
    private Integer age;
    private String bio;

    private Boolean isDeleted;
    private Instant createdTime;
    private Instant updatedTime;

    @DocumentReference
    private List<Preference> preferenceIds;



    public Profile(Map<String, String> profileMap) {
        if (profileMap.get("displayName").isBlank()) {
            throw new IllegalArgumentException("display name cannot be null.");
        }

        // should be dryer

        this.displayName = profileMap.get("displayName");
        this.bio = profileMap.get("bio");
        this.gender = Integer.parseInt(profileMap.get("gender"));
        this.age = Integer.parseInt(profileMap.get("age"));
        this.createdTime = Instant.now();
        this.updatedTime = Instant.now();
    }
}
