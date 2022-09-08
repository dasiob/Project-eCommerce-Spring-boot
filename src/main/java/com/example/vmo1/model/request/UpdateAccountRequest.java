package com.example.vmo1.model.request;

import lombok.Data;

import javax.persistence.Id;

@Data
public class UpdateAccountRequest {
    @Id
    private Long id;
    private String fullname;
    private String phone;
}
