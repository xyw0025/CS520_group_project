package com.group.cs520.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;
import java.time.Instant;
import java.util.List;
import jakarta.validation.constraints.NotBlank;

@Data
@Document(collection = "matches")
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Schema(type="string")
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId id;

    @NotBlank
    private Integer status;

    @NotBlank
    private List<ObjectId> userIds;

    private Instant createdTime;
    private Instant updatedTime;

}