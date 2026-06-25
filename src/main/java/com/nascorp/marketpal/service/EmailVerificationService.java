package com.nascorp.marketpal.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    
    private final StringRedisTemplate redisTemplate;

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from.email}")
    private String fromEmail;

    @Value("${resend.frontend.url}")
    private String frontendUrl;

    private static final String PREFIX = "email-verify: ";
    private static final long TTL_HOURS = 24;

    public void sendVerficationEmail(String email,String username) {
        String sendEmailToken = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
            PREFIX + sendEmailToken, 
            email,
            TTL_HOURS,
            TimeUnit.HOURS
        );

        String verifyLink = frontendUrl + "/verify-email? token = " + sendEmailToken;

        Resend resend = new Resend(resendApiKey);
        CreateEmailOptions emailOptions = CreateEmailOptions.builder()
               .from(fromEmail)
               .to(email)
               .subject("Verify your marketpal account")
               .html(buildEmailTemplate(username, verifyLink))
               .build();

        try {
            resend.emails().send(emailOptions);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email : " + e.getMessage());
        }
    }

    public String verifyToken(String sendEmailToken) {
        String email = redisTemplate.opsForValue().get(PREFIX + sendEmailToken);

        if(email == null) {
            throw new RuntimeException("Invalid or expired verification link");
        }

        redisTemplate.delete(PREFIX + sendEmailToken);

        return email;
    }

    private String buildEmailTemplate(String username, String verifyLink) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2>Welcome to Marketpal, %s!</h2>
                <p>Click the button below to verify your email address.</p>
                <a href="%s"
                   style="background-color: #4F46E5; color: white; padding: 12px 24px;
                          text-decoration: none; border-radius: 6px; display: inline-block;">
                    Verify Email
                </a>
                <p>This link expires in 24 hours.</p>
                <p>If you didn't created this account, ignore this email.</p>
            </div>
                """.formatted(username, verifyLink);
    }
}
