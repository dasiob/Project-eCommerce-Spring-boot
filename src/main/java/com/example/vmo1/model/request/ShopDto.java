package com.example.vmo1.model.request;

import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopDto {
    @Id
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String banner;
    @NotNull
    private String address;
    @NotNull
    private Account account;
    private List<ProductDto> products;
}
