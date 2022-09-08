package com.example.vmo1.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "shop")
@Data
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "banner")
    private String banner;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date created_at;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updated_at;

    @OneToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "shop", cascade= {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Product> lstPro;
}
