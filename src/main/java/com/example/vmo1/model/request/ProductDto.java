package com.example.vmo1.model.request;

import com.example.vmo1.model.entity.Category;
import com.example.vmo1.model.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @Id
    private long id;
    private String name;
    private float price;
    private int quantity;
    private Category category;
    private List<Image> lstImg;
}
