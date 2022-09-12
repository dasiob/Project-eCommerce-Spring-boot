package com.example.vmo1.service.impl;

import com.example.vmo1.model.entity.ConfirmationToken;
import com.example.vmo1.repository.ConfirmationTokenRepository;
import com.example.vmo1.service.ConfirmTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }
    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

//    public int setConfirmedAt(String token) {
//        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
//    }


}
