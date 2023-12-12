package com.group.cs520.service;

import com.group.cs520.model.User;
import com.group.cs520.model.Conversation;
import com.group.cs520.model.Message;
import com.group.cs520.model.UserWithConversationData;
import com.group.cs520.service.MatchService;
import com.group.cs520.repository.ConversationRepository;
import com.group.cs520.repository.MessageRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private MatchService matchService;

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
            // Update last message for both users
            conversation.updateLastMessage(senderId, message);
        } else {
            conversation = new Conversation(senderId, receiverId);
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            conversation.setMessages(messages);

            // Initialize last message and unread count
            conversation.updateLastMessage(senderId, message);
        }
        conversationRepository.save(conversation);
    }

    public List<Message> getConversationMessages(ObjectId user1Id, ObjectId user2Id) {
        Optional<Conversation> foundConversation = conversationRepository.findConversationByUserIds(user1Id, user2Id);
        return foundConversation.map(Conversation::getMessages).orElse(new ArrayList<>());
    }

    public String getConversationId(ObjectId user1Id, ObjectId user2Id) {
        Optional<Conversation> foundConversation = conversationRepository.findConversationByUserIds(user1Id, user2Id);
        return foundConversation.map(conversation -> conversation.getId().toString()).orElse(null);
    }

    public List<UserWithConversationData> getMatchedUsersWithConversationData(ObjectId userId) {

        List<User> matchedUsers = matchService.getMatchedUsers(userId);

        return matchedUsers.stream()
                .map(user -> {
                    UserWithConversationData userWithConversationData = new UserWithConversationData(user);
                    Optional<Conversation> conversation = conversationRepository.findConversationByUserIds(userId, user.getId());
                    conversation.ifPresent(conv -> {
                        Message lastMessage = conv.getLastMessages().get(user.getId());
                        Integer unreadCount = conv.getUnreadCounts().getOrDefault(user.getId(), 0);

                        userWithConversationData.setLastMessage(lastMessage);
                        userWithConversationData.setUnreadCount(unreadCount);
                    });
                    return userWithConversationData;
                })
                .collect(Collectors.toList());
    }

    public void markConversationAsRead(ObjectId userId, ObjectId conversationId) {
        Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
        conversationOpt.ifPresent(conversation -> {
            conversation.markAsRead(userId);
            conversationRepository.save(conversation);
        });
    }
}