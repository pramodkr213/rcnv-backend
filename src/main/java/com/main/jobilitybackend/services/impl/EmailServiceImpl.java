package com.main.jobilitybackend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.helper.EmailFromaters;
import com.main.jobilitybackend.services.serviceInterface.EmailService;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Welcome to Jobility!");
        message.setText("Welcome to Jobility!\n\nThank you for registering with us. We are excited to have you on board.\n\nBest regards,\nJobility Team");

        javaMailSender.send(message);
    }

    @Override
    public void sendOTP(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Dear user,\n\nYour OTP code is: " + otp + "\n\nThis OTP is valid for 5 minutes.\n\nRegards,\nYour Company Name");

        javaMailSender.send(message);
    }


    @Async("taskExecutor")
    @Transactional
    @Override
    public void sendChangeEmailRequest(String email, String newEmail) {
       try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(newEmail);

            helper.setSubject("Email Change Request");
            message.setContent(EmailFromaters.getChangeEmailFormat(email, newEmail), "text/html"); 
            javaMailSender.send(message);
        }catch(Exception e){
            return ;
        }
    }
}