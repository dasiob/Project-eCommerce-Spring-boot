package com.example.vmo1.model.request;

import com.example.vmo1.validation.annotation.MatchPassword;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@MatchPassword
public class PasswordResetRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    @NotNull
    private String token;
}
