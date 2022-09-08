package com.example.vmo1.service;

import com.example.vmo1.model.entity.RefreshToken;

import java.util.Optional;

public interface RefeshTokenService {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken createRefreshToken(Long accountId);
    RefreshToken verifyExpiration(RefreshToken token);
    int deleteByUserId(Long accountId);
}
