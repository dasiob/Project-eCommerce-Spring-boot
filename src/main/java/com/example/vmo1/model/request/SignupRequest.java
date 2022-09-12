package com.example.vmo1.model.request;

import com.example.vmo1.model.entity.Role;
import com.example.vmo1.validation.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.util.Date;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
public class SignupRequest {
    private  String username;
    private String fullname;
    private  String email;
    @ValidPassword
    private  String password;
    private String phone;
    private Set<Role> role;
    private Date created_at;
}
