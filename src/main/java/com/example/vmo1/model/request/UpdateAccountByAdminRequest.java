package com.example.vmo1.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountByAdminRequest {
    @Id
    private Long id;
    private Boolean enable;

}
