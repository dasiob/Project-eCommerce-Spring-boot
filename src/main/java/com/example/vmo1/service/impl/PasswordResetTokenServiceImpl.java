package com.example.vmo1.service.impl;

import com.example.vmo1.commons.exeptions.InvalidTokenRequestException;
import com.example.vmo1.commons.exeptions.ResourceNotFoundException;
import com.example.vmo1.model.entity.PasswordResetToken;
import com.example.vmo1.model.request.PasswordResetRequest;
import com.example.vmo1.repository.PasswordResetTokenRepository;
import com.example.vmo1.service.PasswordReserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetTokenServiceImpl implements PasswordReserTokenService {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Override
    public PasswordResetToken getValidToken(PasswordResetRequest request){
        String tokenName = request.getToken();
        PasswordResetToken token = passwordResetTokenRepository.findByToken(tokenName).orElseThrow(() -> new ResourceNotFoundException("Token reset token", "Token", tokenName));
        matchEmail(token, request.getEmail());
        verifyExpiration(token);
        return token;
    }
    @Override
    public void verifyExpiration(PasswordResetToken request){
        if (request.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenRequestException("Password Reset Token", request.getToken(),
                    "Expired token. Please issue a new request");
        }
        if(!request.getActive()){
            throw new InvalidTokenRequestException("Password Reset Token", request.getToken(),
                    "Token was marked inactive");
        }
    }
    @Override
    public void inIsActiveToken(PasswordResetRequest request){
        String token = request.getToken();
        passwordResetTokenRepository.updateActive(token,false);
    }
    @Override
    public void matchEmail(PasswordResetToken token, String requestEmail){
        if(token.getAccount().getEmail().compareToIgnoreCase(requestEmail) != 0){
            throw new InvalidTokenRequestException("Password Reset Token", token.getToken(),
                    "Token is invalid for the given user " + requestEmail);
        }

    }


}
