package com.odoo.communitypulse.user.controllers;

import com.odoo.communitypulse.user.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test-email")
    public String testEmail() {
        emailService.sendEmail("kamllesh.yadavv@gmail.com", "Test Email", "Hello from Spring Boot");
        return "Test email sent.";
    }
}
