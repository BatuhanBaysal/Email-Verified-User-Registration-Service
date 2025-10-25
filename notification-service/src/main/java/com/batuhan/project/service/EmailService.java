package com.batuhan.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.from}")
    private String fromAddress;

    public void sendConfirmationEmail(String toEmail, String token) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Confirm Your Email Address - Batuhan Project");
        String confirmationLink = "http://localhost:8080/api/v1/auth/confirm?token=" + token;

        message.setText(String.format(
                "Hello,\n\n" +
                        "Please click the link below to complete your registration:\n" +
                        "%s\n\n" +
                        "This link will expire in 15 minutes.",
                confirmationLink
        ));

        try {
            mailSender.send(message);
            System.out.println("EMAIL SERVICE: Confirmation email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("EMAIL SERVICE ERROR: Failed to send email to " + toEmail + ". Error: " + e.getMessage());
        }
    }
}