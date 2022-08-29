package com.example.vmo1.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account",  uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
@Setter
@Getter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullname;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updated_at;

    @Column(name = "is_deleted")
    private boolean is_deleted;

    @Column(name="enabled")
    private boolean enabled;

    @Column(name="locked")
    private Boolean locked;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private ERole accountRole;
    private Set<Role> roles = new HashSet<>();
    
    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public Account(String fullname, String password, String email, boolean enabled, Boolean locked, Set<Role> roles) {
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.locked = locked;
        this.roles = roles;
    }

    public Account(String fullname, String password, String email, Set<Role> roles) {
        this.fullname = fullname;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }
}

