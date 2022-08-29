package com.example.vmo1.model.request;

import com.example.vmo1.validation.annotation.MatchPassword;
import lombok.Data;

@Data
@MatchPassword
public class PasswordResetRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String token;
}
