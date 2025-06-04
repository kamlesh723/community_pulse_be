package com.odoo.communitypulse.user.service;

import com.odoo.communitypulse.user.entity.Otp;
import com.odoo.communitypulse.user.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    public String generateOtp(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.deleteByEmail(email); // Remove old OTPs
        otpRepository.save(otp);
        return code;
    }

    public boolean validateOtp(String email, String code) {
        return otpRepository.findByEmailAndCode(email, code)
                .filter(o -> o.getExpiryTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public void deleteOtp(String email) {
        otpRepository.deleteByEmail(email);
    }
}
