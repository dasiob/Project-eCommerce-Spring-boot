package com.example.vmo1.service.impl;

import com.example.vmo1.service.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailSender {
    @Autowired
    private JavaMailSender mailSender;
    private final static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    @Async
    public void sendEmail(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("huent1@vmodev.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Failed to send email for: " + email + "\n" + e);
            throw new IllegalArgumentException("Failed to send email for: " + email);
        }
    }
}
