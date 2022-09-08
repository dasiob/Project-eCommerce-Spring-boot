package com.example.vmo1.service;

import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.request.SignupRequest;
import com.example.vmo1.model.response.MessageResponse;

public interface RegistrationService {
    void saveConfirmationToken(Account account, String token);
    MessageResponse register(SignupRequest request);
    String confirmToken(String token);
    String buildEmail(String name, String link);
}
