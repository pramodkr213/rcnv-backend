package com.main.jobilitybackend.services.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.jobilitybackend.entities.OtpEntry;
import com.main.jobilitybackend.repositories.OtpEntryRepo;
import com.main.jobilitybackend.services.serviceInterface.EmailService;
import com.main.jobilitybackend.services.serviceInterface.OtpService;

@Service
public class OtpServiceImpl implements OtpService{
    
    @Autowired
    private OtpEntryRepo otpRepo;

    @Autowired
    private EmailService emailService;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    @Override
    public String generateAndSendOtp(String email) {

        otpRepo.deleteByEmail(email);
        String otp = generateOtp();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);
        LocalDateTime sessionTime = LocalDateTime.now().plusMinutes(5);

        OtpEntry otpEntry = new OtpEntry();
        otpEntry.setEmail(email);
        otpEntry.setOtp(otp);
        otpEntry.setExpirationTime(expirationTime);
        otpEntry.setSessionTime(sessionTime);
        otpRepo.save(otpEntry);

        this.emailService.sendOTP(email, otp);
        return otp;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        OtpEntry otpEntry = otpRepo.findByEmail(email).orElse(null);
        return otpEntry != null && !otpEntry.isExpired() && otpEntry.getOtp().equals(otp);
    }

    @Override
    public boolean varifySession(String email,String otp){
        OtpEntry otpEntry = otpRepo.findByEmail(email).orElse(null);
        return otpEntry != null && !otpEntry.isSessionExpired() && otpEntry.getOtp().equals(otp);
    }

    @Override
    public String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(RANDOM.nextInt(10));
        }
        return otp.toString();
    }



    @Override
    public boolean verifEmail(String email, String otp) {
       OtpEntry otpEntry = otpRepo.findByEmail(email).orElse(null);
        return otpEntry != null && !otpEntry.isExpired() && otpEntry.getOtp().equals(otp);
    }
}
