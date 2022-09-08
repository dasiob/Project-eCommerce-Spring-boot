package com.example.vmo1.model.request;

import com.example.vmo1.model.entity.Role;
import com.example.vmo1.validation.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    private  String username;
    @Email
    private  String email;
    @ValidPassword
    private  String password;
    private Set<Role> role;
}
