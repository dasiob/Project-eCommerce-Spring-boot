package com.example.vmo1.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountByAdminRequest {

    private String fullname;
    private String username;
    private String email;
    private String password;
    private String phone;
    private Boolean enable;
}
