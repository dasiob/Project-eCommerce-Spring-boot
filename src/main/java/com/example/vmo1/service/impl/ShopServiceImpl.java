package com.example.vmo1.service.impl;

import com.example.vmo1.commons.configs.MapperUtil;
import com.example.vmo1.model.entity.Shop;
import com.example.vmo1.model.request.ShopDto;
import com.example.vmo1.repository.ShopRepository;
import com.example.vmo1.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopRepository shopRepository;

    @Value("${project.image}")
    private String path;
    @Override
    public ShopDto save(ShopDto shopDto, MultipartFile file) throws IOException {
        Shop shop = MapperUtil.map(shopDto, Shop.class);
        String name = file.getOriginalFilename();

        // random name generate file
        String randomID = UUID.randomUUID().toString();
        String filename1 = randomID.concat(name.substring(name.lastIndexOf(".")));
        String filePath = path + File.separator + filename1;

        File f = new File(path);
        if(!f.exists()){
            f.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));

        shop.setBanner(name);
        shopRepository.save(shop);
        return MapperUtil.map(shop, ShopDto.class);
    }

}
