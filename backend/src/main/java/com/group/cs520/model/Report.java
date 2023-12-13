package com.group.cs520.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.Instant;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.lang.reflect.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reports")
public class Report {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(type="string")
    @Id
    private ObjectId id;
    private String reportContent;
    private Instant createdTime;

    public Report(String reportContent) {
        this.reportContent = reportContent;
        this.createdTime = Instant.now();
    }
}
