package com.example.vmo1.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
@Data
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column
    private LocalDateTime confirmedAt;

    @Column
    private Boolean active;
    @ManyToOne
    @JoinColumn(nullable = false,
            name = "account_id")
    private Account account;

    public PasswordResetToken(String token, LocalDateTime expiresAt, Boolean active, Account account) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.active = active;
        this.account = account;
    }
}
