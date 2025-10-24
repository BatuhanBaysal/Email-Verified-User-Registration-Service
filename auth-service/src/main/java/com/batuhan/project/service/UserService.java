package com.batuhan.project.service;

import com.batuhan.project.entity.UserEntity;
import com.batuhan.project.event.UserRegisteredEvent;
import com.batuhan.project.exception.EmailAlreadyExistsException;
import com.batuhan.project.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String USER_REGISTRATION_TOPIC = "user-registration-queue";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public UserEntity saveNewUser(String email, String rawPassword, String username) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword));

        UserEntity savedUser = userRepository.save(newUser);

        UserRegisteredEvent event = new UserRegisteredEvent(
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getCreatedAt()
        );

        try {
            String jsonEvent = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(USER_REGISTRATION_TOPIC, jsonEvent);
            System.out.println("AUTH SERVICE (UserService): Publishing JSON string: " + jsonEvent);

        } catch (JsonProcessingException e) {
            System.err.println("ERROR: Failed to serialize UserRegisteredEvent to JSON string.");
            throw new RuntimeException("Redis event serialization failed.", e);
        }

        return savedUser;
    }
}