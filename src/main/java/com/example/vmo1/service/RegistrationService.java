package com.example.vmo1.service;

import com.example.vmo1.model.request.SignupRequest;
import com.example.vmo1.model.response.MessageResponse;

public interface RegistrationService {
    MessageResponse register(SignupRequest request);
    String confirmToken(String token);
    String buildEmail(String name, String link);
}
