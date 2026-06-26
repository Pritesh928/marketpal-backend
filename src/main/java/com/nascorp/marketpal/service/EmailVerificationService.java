package com.nascorp.marketpal.service;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationService {

    private final StringRedisTemplate redisTemplate;

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from.email}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailVerificationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String PREFIX = "email-verify:";
    private static final long TTL_HOURS = 24;

    
    public void sendVerificationEmail(String email, String username) {
        
        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
            PREFIX + token,
            email,
            TTL_HOURS,
            TimeUnit.HOURS
        );

        String verifyLink = frontendUrl + "/verify-email?token=" + token;

        Resend resend = new Resend(resendApiKey);
        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(email)
                .subject("Verify your MarketPal account")
                .html(buildEmailTemplate(username, verifyLink))
                .build();

        try {
            resend.emails().send(emailOptions);
        } catch (Exception e) {
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
                <h2>Welcome to MarketPal, %s!</h2>
                <p>Click the button below to verify your email address.</p>
                <a href="%s"
                   style="background-color: #4F46E5; color: white; padding: 12px 24px;
                          text-decoration: none; border-radius: 6px; display: inline-block;">
                    Verify Email
                </a>
                <p>This link expires in 24 hours.</p>
                <p>If you didn't create this account, ignore this email.</p>
            </div>
            """.formatted(username, verifyLink);
    }
}