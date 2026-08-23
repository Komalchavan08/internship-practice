package com.example.StudentManagementAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("StudentHub — Password Reset OTP");
        message.setText(
                "Your one-time password (OTP) to reset your StudentHub account password is:\n\n"
                        + otp + "\n\n"
                        + "This code expires in 10 minutes. If you didn't request this, you can safely ignore this email."
        );

        mailSender.send(message);
    }
}