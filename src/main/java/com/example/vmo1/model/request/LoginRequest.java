package com.example.vmo1.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class LoginRequest {

    private String usernameOrEmail;


    private String password;


}
