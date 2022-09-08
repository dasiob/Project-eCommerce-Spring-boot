package com.example.vmo1.controller;

import com.example.vmo1.model.entity.Shop;
import com.example.vmo1.model.request.ShopDto;
import com.example.vmo1.model.response.ShopResponse;
import com.example.vmo1.service.ShopService;
import com.example.vmo1.service.impl.ShopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestPart MultipartFile file, @RequestPart ShopDto shopDto) {
        return ResponseEntity.ok(shopService.add(shopDto, file));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestPart ShopDto shopDto, @PathVariable("id") long id, @RequestPart MultipartFile file){
        ShopDto shopResponse = shopService.update(shopDto, id, file);
        return new ResponseEntity<>(shopResponse, HttpStatus.OK);
    }


}
