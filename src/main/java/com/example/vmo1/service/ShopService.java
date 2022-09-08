package com.example.vmo1.service;

import com.example.vmo1.model.request.ShopDto;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.model.response.ShopResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ShopService {
    MessageResponse add(ShopDto shopDto, MultipartFile file);
    ShopDto update(ShopDto shopDto, long id, MultipartFile file);
    ShopResponse getAllShop(int pageNo, int pageSize);
}
