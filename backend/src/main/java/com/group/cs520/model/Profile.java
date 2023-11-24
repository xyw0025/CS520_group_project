package com.group.cs520.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;

import com.group.cs520.service.DateUtil;
import com.group.cs520.service.TypeUtil;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "profiles")
public class Profile {
    @Id
    private ObjectId id;
    private String displayName;
    private Integer gender;
    private LocalDate birthday;
    private Integer age;

    @Size(min=0, max=4)
    private List<String> imageUrls;

    @Size(max=300)
    private String bio;

    private Boolean isDeleted;
    private Instant createdTime;
    private Instant updatedTime;

    @DocumentReference
    private List<Preference> preferences;


    public Profile(Map<String, String> profileMap) {
        // should be dryer
        this.displayName = profileMap.get("displayName");
        this.gender = Integer.parseInt(profileMap.get("gender"));
        this.birthday = DateUtil.dateFormatter(profileMap.get("birthday"), "yyyy-MM-dd");
        this.age = Integer.parseInt(profileMap.get("age"));
        this.imageUrls = TypeUtil.jsonStringArray(profileMap.get("imageUrls"));
        this.bio = profileMap.get("bio");
        this.createdTime = Instant.now();
        this.updatedTime = Instant.now();
        this.isDeleted = false;
    }
}
