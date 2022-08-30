package com.example.vmo1.service;

import com.example.vmo1.model.entity.ConfirmationToken;

import java.util.Optional;

public interface ConfirmTokenService {
    void saveConfirmationToken(ConfirmationToken token);
    Optional<ConfirmationToken> getToken(String token);

}
