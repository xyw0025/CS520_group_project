package com.group.cs520.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.time.Instant;
import jakarta.validation.constraints.NotBlank;

@Data
@Document(collection = "preferences")
@NoArgsConstructor
@AllArgsConstructor
public class Preference {
    @Id
    private ObjectId id;

    @NotBlank
    private Integer categoryId;

    @NotBlank
    private String name;

    private Instant createdTime;
    private Instant updatedTime;

    public Preference(Map<String, String> perferenceMap) {
        this.categoryId = Integer.parseInt(perferenceMap.get("categoryId"));
        this.name = perferenceMap.get("name");
        this.createdTime = Instant.now();
        this.updatedTime = Instant.now();
    }
}
