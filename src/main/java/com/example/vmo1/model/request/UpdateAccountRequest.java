package com.example.vmo1.model.request;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class UpdateAccountRequest {
    private String fullname;
    private String phone;
}
