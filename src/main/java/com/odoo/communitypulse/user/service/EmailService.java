package com.odoo.communitypulse.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void sendOtpEmail(String to, String otp) {
        String subject = "Your OTP Code";
        String text = "Your OTP is: " + otp;
        sendEmail(to, subject, text);
    }
}
