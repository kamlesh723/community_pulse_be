package com.odoo.communitypulse.user.request;

public class OtpRequest {
    private String email;
    private String otp;

    // Getters
    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
