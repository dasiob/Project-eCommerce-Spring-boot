package com.example.vmo1.service;

import com.example.vmo1.model.request.ShopDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ShopService {
    ShopDto save(ShopDto shopDto, MultipartFile file);
}
