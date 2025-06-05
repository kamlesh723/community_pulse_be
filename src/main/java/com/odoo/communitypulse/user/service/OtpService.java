package com.odoo.communitypulse.user.service;

import com.odoo.communitypulse.user.entity.Otp;
import com.odoo.communitypulse.user.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Transactional
    public String generateOtp(String email) {
        // Generate 6-digit OTP
        String code = String.format("%06d", new Random().nextInt(999999));

        // Remove any existing OTP for this email
        otpRepository.deleteByEmail(email);

        // Create and save new OTP
        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otp);

        return code;
    }

    public boolean validateOtp(String email, String code) {
        Optional<Otp> otpOptional = otpRepository.findByEmailAndCode(email, code);
        return otpOptional.filter(o -> o.getExpiryTime().isAfter(LocalDateTime.now())).isPresent();
    }

    @Transactional
    public void deleteOtp(String email) {
        otpRepository.deleteByEmail(email);
    }
}
