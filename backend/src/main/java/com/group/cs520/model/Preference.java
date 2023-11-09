package com.group.cs520.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "preferences")
@NoArgsConstructor
@AllArgsConstructor
public class Preference {
    @Id
    private ObjectId id;
    private Integer categoryId;
    private String name;
}
