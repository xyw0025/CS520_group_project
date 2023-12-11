package com.group.cs520.service;

import com.group.cs520.model.Conversation;
import com.group.cs520.model.Message;
import com.group.cs520.repository.ConversationRepository;
import com.group.cs520.repository.MessageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public void handleConversationForMessage(Message message) {
        ObjectId senderId = message.getSenderId();
        ObjectId receiverId = message.getReceiverId();

        // Find existing conversation or create a new one
        Optional<Conversation> existingConversation = conversationRepository
                .findConversationByUserIds(senderId, receiverId);

        Conversation conversation;
        if (existingConversation.isPresent()) {
            conversation = existingConversation.get();
            conversation.getMessages().add(message);
        } else {
            conversation = new Conversation(senderId, receiverId);
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            conversation.setMessages(messages);
        }
        conversationRepository.save(conversation);
    }

    public List<Message> getConversationMessages(ObjectId user1Id, ObjectId user2Id) {
        Optional<Conversation> conversation = conversationRepository.findConversationByUserIds(user1Id, user2Id);
        return conversation.map(Conversation::getMessages).orElse(new ArrayList<>());
    }
}