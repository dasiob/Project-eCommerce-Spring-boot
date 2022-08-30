package com.example.vmo1.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @Id
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private String password;
    private Date create_at;
    private Date updated_at;
}
