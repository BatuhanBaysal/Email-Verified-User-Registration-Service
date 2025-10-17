package com.batuhan.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import com.batuhan.project.event.UserRegisteredEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
public class EmailConfirmationListener {

    public void handleMessage(String message) {
        System.out.println("-------------------------------------");
        System.out.println("NOTIFICATION SERVICE:");
        System.out.println("New Message Received (String): " + message);
        System.out.println("-------------------------------------");
    }
}