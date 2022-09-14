package com.example.vmo1.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TokenRefreshRequest {
    @NotNull
    private String refeshToken;
}
