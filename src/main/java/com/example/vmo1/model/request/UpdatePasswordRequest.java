package com.example.vmo1.model.request;

import lombok.Data;

import java.util.Date;

@Data
public class UpdatePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private Date updated_at;
}
