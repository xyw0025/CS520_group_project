package com.group.cs520.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.group.cs520.model.Message;
import com.group.cs520.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/sendMessage")
    @SendTo("/room/messages")
    public Message sendMessage(@Payload Message chatMessage) {
        Message savedMessage = messageRepository.save(chatMessage);

        return savedMessage;
    }
}