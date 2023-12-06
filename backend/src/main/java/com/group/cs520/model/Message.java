package com.group.cs520.model;

import java.time.LocalDateTime;
import org.bson.types.ObjectId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@Document(collection = "messages")
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private ObjectId messageId;
    private ObjectId senderId;
    private ObjectId receiverId;
    private String messageText;
    private Instant timestamp;


    public Message(ObjectId messageId, ObjectId senderId, ObjectId receiverId, String messageText) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = Instant.now();
    }
}