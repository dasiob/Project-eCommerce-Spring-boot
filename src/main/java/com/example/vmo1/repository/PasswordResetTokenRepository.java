package com.example.vmo1.repository;

import com.example.vmo1.model.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE PasswordResetToken c SET c.confirmedAt = ?2 WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime localDateTime);
    @Transactional
    @Modifying
    @Query(value = "UPDATE PasswordResetToken c SET c.active = ?2 WHERE c.token = ?1")
    void updateActive(String token, boolean active);
}
