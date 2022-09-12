package com.example.vmo1.model.request;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class UpdateAccountRequest {
    @Id
    private Long id;
    private String fullname;
    private String phone;
    private Date updated_at;
}
