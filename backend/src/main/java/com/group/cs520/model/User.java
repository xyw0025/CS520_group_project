package com.group.cs520.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;
import java.util.List;
import java.util.TimeZone;


@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;
    private String name;

    @NotBlank
    private String email;
    @NotBlank
    @JsonIgnore
    private String password;

    private Boolean isActive;
    private Boolean isDeleted;
    private Instant createdTime;
    private Instant updatedTime;

    @DocumentReference
    private List<Profile> profileIds;

    public User(String email,String password) {
        if (email.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Name and password cannot be null.");
        }
        this.email = email;
        this.password = password;
        this.createdTime = Instant.now(); 
        this.updatedTime = Instant.now();

    }
}
