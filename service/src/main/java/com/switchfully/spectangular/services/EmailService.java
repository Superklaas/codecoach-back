package com.switchfully.spectangular.services;

import com.switchfully.spectangular.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }

    void sendEmailToResetPassword(User user, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("spectangular.codecoach@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n"
                + url + "/reset-password?token=" + user.getResetToken()
                + "\nThis link will expire in 30 minutes.");
        sendEmail(message);
    }
}
