package com.group.cs520.model;

import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "conversations")
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @Schema(type="string")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private ObjectId user1Id;
    private ObjectId user2Id;
    @DocumentReference
    private List<Message> messages;
    private Instant createdAt = Instant.now();

    public Conversation(ObjectId user1Id, ObjectId user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.createdAt = Instant.now();
    }
}