package com.main.jobilitybackend.services.serviceInterface;

public interface EmailService {
    void sendMail(String email);
    void sendOTP(String email,String otp);
    void sendChangeEmailRequest(String email, String newEmail);
}