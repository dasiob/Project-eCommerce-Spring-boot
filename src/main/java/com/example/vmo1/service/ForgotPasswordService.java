package com.example.vmo1.service;

import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.request.PasswordResetLinkRequest;
import com.example.vmo1.model.request.PasswordResetRequest;
import com.example.vmo1.model.response.MessageResponse;

public interface ForgotPasswordService {
    String confirmTokenResetPassword(String token);
    String sendTokenToChangePassword(PasswordResetLinkRequest request);
    MessageResponse changePassword(PasswordResetRequest request);
    boolean updatePassword(String email, String password);
    void saveConfirmationToken(Account account, String token);
    String buildEmail(String name, String link);
}
