package com.odoo.communitypulse.user.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String username;
    private String phone;
    private String password;


}
