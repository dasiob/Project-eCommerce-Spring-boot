package com.example.vmo1.model.request;

import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDto {
    @Id
    private Long id;
    private String name;
    private String banner;
    private String address;
    private Account account;
    private List<Product> products;
}
