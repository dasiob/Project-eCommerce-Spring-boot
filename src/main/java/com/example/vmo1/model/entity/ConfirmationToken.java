package com.example.vmo1.model.entity;

import com.example.vmo1.model.entity.Account;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updated_at;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Column
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false,
            name = "account_id")
    private Account account;

    public ConfirmationToken() {
    }

    public ConfirmationToken(String token, LocalDateTime expiresAt, Account account) {
        this.token = token;
        this.expiresAt = expiresAt;
        this.account = account;
    }



}
