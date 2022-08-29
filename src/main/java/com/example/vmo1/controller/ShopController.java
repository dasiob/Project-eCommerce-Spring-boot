package com.example.vmo1.controller;

import com.example.vmo1.model.request.ShopDto;
import com.example.vmo1.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;
    @PostMapping("/add")
    public ResponseEntity<?> save(@RequestPart ShopDto shopDto,@RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(shopService.save(shopDto, file));
    }
}
