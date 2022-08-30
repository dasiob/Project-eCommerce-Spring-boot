package com.example.vmo1.service;

import com.example.vmo1.model.entity.PasswordResetToken;
import com.example.vmo1.model.request.PasswordResetRequest;
import com.example.vmo1.model.response.MessageResponse;

public interface PasswordReserTokenService {
    PasswordResetToken getValidToken(PasswordResetRequest request);
    void verifyExpiration(PasswordResetToken request);
    void inIsActiveToken(PasswordResetRequest request);
    void matchEmail(PasswordResetToken token, String requestEmail);
}
