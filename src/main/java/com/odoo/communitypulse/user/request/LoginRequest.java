package com.odoo.communitypulse.user.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}

