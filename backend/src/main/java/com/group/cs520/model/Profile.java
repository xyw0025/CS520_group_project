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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.group.cs520.service.DateUtil;
import com.group.cs520.service.TypeUtil;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "profiles")
public class Profile {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;
    private String displayName = "New User";;
    private Integer gender;
    private LocalDate birthday;
    private Integer age;

    @Size(min=0, max=4)
    private List<String> imageUrls;

    @Size(max=30)
    private String major;

    @Size(max=300)
    private String bio;

    private Boolean isDeleted = false;;
    private Instant createdTime = Instant.now();
    private Instant updatedTime = Instant.now();

    @DocumentReference
    private List<Preference> preferences;

    public Profile(Map<String, Object> profileMap) {
        this.displayName = (String) profileMap.get("displayName");
        this.gender = Integer.parseInt((String) profileMap.get("gender"));
        this.birthday = DateUtil.dateFormatter((String) profileMap.get("birthday"), "yyyy-MM-dd");
        this.age = Integer.parseInt((String) profileMap.get("age"));
        this.imageUrls = TypeUtil.objectToListString(profileMap.get("imageUrls"));
        this.major = (String) profileMap.get("major");
        this.bio = (String) profileMap.get("bio");
        this.createdTime = Instant.now();
        this.updatedTime = Instant.now();
        this.isDeleted = false;
    }
}
