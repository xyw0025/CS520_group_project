package com.group.cs520.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "profiles")
public class Profile {
    @Id
    private ObjectId id;
    private String displayName;
    private Integer gender;
    private Integer age;
    private String bio;

    private Boolean isDeleted;
    private Instant createdTime;
    private Instant updatedTime;

    @DocumentReference
    private List<Preference> preferenceIds;
}
