package com.example.vmo1.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "password_reset_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @CreationTimestamp
    @Column(name = "created_at")
    private Date created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updated_at;
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
