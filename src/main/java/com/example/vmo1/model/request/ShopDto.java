package com.example.vmo1.model.request;

import com.example.vmo1.model.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDto {
    @Id
    private Long id;
    private String name;
    private String banner;
    private Account account;
}
