package com.example.vmo1.service;

import com.example.vmo1.model.request.ProductDto;
import com.example.vmo1.model.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductDto save(ProductDto productDto, MultipartFile[] files);
    ProductDto updateProduct(ProductDto productDto, long id, MultipartFile[] files);
    ProductResponse getAllProduct(int pageNo, int pageSize);
    void deleteProduct(long id);
}
