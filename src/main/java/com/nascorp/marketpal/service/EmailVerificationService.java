package com.nascorp.marketpal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationService {

    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private static final String PREFIX = "email-verify:";
    private static final long TTL_HOURS = 24;

    public EmailVerificationService(StringRedisTemplate redisTemplate,
                                     JavaMailSender mailSender) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String email, String username) {

        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
            PREFIX + token,
            email,
            TTL_HOURS,
            TimeUnit.HOURS
        );

        String verifyLink = frontendUrl + "/verify-email?token=" + token;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Verify your MarketPal account");
            helper.setText(buildEmailTemplate(username, verifyLink), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }
    }

    public String verifyToken(String token) {
        String email = redisTemplate.opsForValue().get(PREFIX + token);

        if (email == null) {
            throw new RuntimeException("Invalid or expired verification link");
        }

        redisTemplate.delete(PREFIX + token);
        return email;
    }

    private String buildEmailTemplate(String username, String verifyLink) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                    <h2 style="color: #4F46E5;">Welcome to MarketPal, %s!</h2>
                    <p>Click the button below to verify your email address.</p>
                    <a href="%s"
                       style="background-color: #4F46E5; color: white; padding: 12px 24px;
                              text-decoration: none; border-radius: 6px; display: inline-block;
                              margin: 16px 0;">
                        Verify Email
                    </a>
                    <p style="color: #6B7280;">This link expires in 24 hours.</p>
                    <p style="color: #6B7280;">If you didn't create this account, ignore this email.</p>
                </div>
                """.formatted(username, verifyLink);
    }
}