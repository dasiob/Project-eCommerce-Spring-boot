package com.example.vmo1.service;

import com.example.vmo1.model.entity.PasswordResetToken;
import com.example.vmo1.model.request.PasswordResetRequest;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetTokenService {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetToken getValidToken(PasswordResetRequest request){
        String tokenName = request.getToken();
        PasswordResetToken token = passwordResetTokenRepository.findByToken(tokenName).get();
        matchEmail(token, request.getEmail());
        verifyExpiration(token);
        return token;
    }

    public void verifyExpiration(PasswordResetToken request){
        if (request.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token is already expired!");
        }
        if(!request.getActive()){
            throw new IllegalStateException("Token was marked is inactive");
        }
    }

    public void inIsActiveToken(PasswordResetRequest request){
        String token = request.getToken();
        passwordResetTokenRepository.updateActive(token,false);
    }

    public MessageResponse matchEmail(PasswordResetToken token, String requestEmail){
        if(token.getAccount().getEmail().compareToIgnoreCase(requestEmail) != 0){
            return new MessageResponse("Token is invalid for given account");
        }
        return new MessageResponse("Token is valid");
    }


}
