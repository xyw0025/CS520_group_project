package com.group.cs520.service;

import com.group.cs520.model.Conversation;
import com.group.cs520.model.Message;
import com.group.cs520.repository.ConversationRepository;
import com.group.cs520.repository.MessageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private ConversationRepository conversationRepository;

    public void handleConversationForMessage(Message message) {
        ObjectId senderId = message.getSenderId();
        ObjectId receiverId = message.getReceiverId();
        ObjectId messageId = message.getId();

        // Find existing conversation or create a new one
        Optional<Conversation> existingConversation = conversationRepository
                .findConversationByUserIds(senderId, receiverId);

        Conversation conversation;
        if (existingConversation.isPresent()) {
            conversation = existingConversation.get();
            conversation.getMessageIds().add(messageId);
        } else {
            conversation = new Conversation(senderId, receiverId);
            List<ObjectId> messageIds = new ArrayList<>();
            messageIds.add(messageId);
            conversation.setMessageIds(messageIds);
        }

        // Save the conversation
        conversationRepository.save(conversation);
    }
}