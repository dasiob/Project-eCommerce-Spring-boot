package com.example.vmo1.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdatePasswordRequest {
    @NotNull
    private String oldPassword;
    @NotNull
    private String newPassword;
}
