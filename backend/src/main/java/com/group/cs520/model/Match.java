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
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;

import com.group.cs520.service.TypeUtil;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "matches")
public class Match {
    @Id
    private ObjectId id;

    private List<ObjectId> userIds;
    private Integer status;

    @Size(min=0, max=2)
    private List<String> history;
    
    private Boolean isDeleted;
    private Instant createdTime;
    private Instant updatedTime;

    @DocumentReference
    private List<MatchHistory> matchHistories;

    public Match(Map<String, Object> matchMap) {
        Class<?> matchClass = Match.class;
        Field[] fields = matchClass.getDeclaredFields();
        List<String> fieldsToSkip = Arrays.asList("userIds", "id", "isDeleted", "createdTime", "updatedTime");

        for (Field field: fields) {
            String fieldName = field.getName();
            if (!fieldsToSkip.contains(fieldName)) {
                TypeUtil.setField(this, field, matchMap.get(fieldName));
            }
        }

        this.createdTime = Instant.now();
        this.updatedTime = Instant.now();
        this.isDeleted = false;
        this.userIds = TypeUtil.objectIdArray(matchMap.get("userIds").toString());
    }
}
