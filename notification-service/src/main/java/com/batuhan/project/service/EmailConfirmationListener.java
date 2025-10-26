package com.batuhan.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import com.batuhan.project.event.UserRegisteredEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class EmailConfirmationListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConfirmationTokenService tokenService;

    @PostConstruct
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void handleMessage(String message) {
        System.out.println("-------------------------------------");
        System.out.println("NOTIFICATION SERVICE: Starting processing JSON...");

        try {
            UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);
            System.out.println("Message Successfully Parsed (POJO). Email: " + event.getEmail());
            tokenService.createAndSaveToken(event.getEmail());

        } catch (JsonProcessingException e) {
            System.err.println("ERROR: Failed to parse incoming JSON message: " + e.getMessage());
        }
        System.out.println("-------------------------------------");
    }
}