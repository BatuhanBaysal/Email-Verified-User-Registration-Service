package com.batuhan.project.service;

import com.batuhan.project.entity.ConfirmationToken;
import com.batuhan.project.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository tokenRepository;
    private final EmailService emailService;

    public ConfirmationToken createAndSaveToken(String userEmail) {
        String token = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(15);

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                createdAt,
                expiresAt,
                userEmail
        );

        tokenRepository.save(confirmationToken);
        System.out.println("TOKEN SERVICE: Created and saved token for " + userEmail);
        emailService.sendConfirmationEmail(userEmail, confirmationToken.getToken());
        return confirmationToken;
    }
}