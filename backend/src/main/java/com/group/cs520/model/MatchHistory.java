package com.group.cs520.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.lang.reflect.Field;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.group.cs520.constants.MatchConstants;
import com.group.cs520.service.TypeUtil;

import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "matchHistories")
public class MatchHistory {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type="string")
    private ObjectId id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type="string")
    @NotBlank
    private ObjectId senderId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type="string")
    @NotBlank
    private ObjectId receiverId;

    @NotBlank
    private Integer behavior;

    private Boolean isDeleted;
    private Instant createdTime;
    private Instant updatedTime;

    public MatchHistory(String senderId, String receiverId, String behavior) {
        this.senderId = TypeUtil.objectIdConverter(senderId);
        this.receiverId = TypeUtil.objectIdConverter(receiverId);
        this.behavior = MatchConstants.BEHAVIOR.valueOf(behavior.toUpperCase()).ordinal();
        this.createdTime = Instant.now();
        this.updatedTime = Instant.now();
        this.isDeleted = false;
    }
}
