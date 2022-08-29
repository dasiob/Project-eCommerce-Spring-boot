package com.example.vmo1.service;

import com.example.vmo1.model.request.ShopDto;
import com.example.vmo1.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;

    public ShopDto save(ShopDto shopDto, MultipartFile file){

    }
}
