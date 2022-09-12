package com.example.vmo1.service.impl;

import com.example.vmo1.commons.configs.MapperUtil;
import com.example.vmo1.commons.exceptions.ResourceNotFoundException;
import com.example.vmo1.model.entity.Shop;
import com.example.vmo1.model.request.ShopDto;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.model.response.ShopResponse;
import com.example.vmo1.repository.ShopRepository;
import com.example.vmo1.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopRepository shopRepository;

    @Value("${project.image}")
    private String path;
    @Override
    public MessageResponse add(ShopDto shopDto, MultipartFile file) {
        Shop shop = MapperUtil.map(shopDto, Shop.class);
        boolean isAccountExist = shopRepository.findByAccountId(shop.getAccount().getId()).isPresent();
        if(isAccountExist){
            return new MessageResponse("Account already use");
        }
        try {
            String name = file.getOriginalFilename();

            // random name generate file
            String randomID = UUID.randomUUID().toString();
            String filename1 = randomID.concat(name.substring(name.lastIndexOf(".")));
            String filePath = path + File.separator + filename1;

            File f = new File(path);
            if (!f.exists()) {
                f.mkdir();
            }
            Files.copy(file.getInputStream(), Paths.get(filePath));
            // save image to field banner in shop
            shop.setBanner(name);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        shopRepository.save(shop);
        MapperUtil.map(shop, ShopDto.class);
        return new MessageResponse("Create shop successfully");
    }

    @Override
    public ShopDto update(ShopDto shopDto, long id, MultipartFile file) {
        Shop shop = MapperUtil.map(shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Find shop by id", "Shop", id)), Shop.class);

        return MapperUtil.map(shopRepository.save(shop), ShopDto.class);
    }

    @Override
    public ShopResponse getAllShop(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Shop> shops = shopRepository.findAll(pageable);
        List<Shop> shopList = shops.getContent();
        List<ShopDto> content = shopList.stream().map(shop -> MapperUtil.map(shop, ShopDto.class)).collect(Collectors.toList());
        ShopResponse shopResponse = new ShopResponse();
        shopResponse.setContent(content);
        shopResponse.setPageNo(shops.getNumber());
        shopResponse.setPageSize(shops.getSize());
        shopResponse.setTotalElements(shops.getTotalElements());
        shopResponse.setTotalPages(shops.getTotalPages());
        shopResponse.setLast(shops.isLast());
        return shopResponse;
    }

}
