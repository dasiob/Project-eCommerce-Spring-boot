package com.example.vmo1.service.impl;

import com.example.vmo1.commons.configs.MapperUtil;
import com.example.vmo1.commons.exceptions.ResourceNotFoundException;
import com.example.vmo1.model.entity.Image;
import com.example.vmo1.model.entity.Product;
import com.example.vmo1.model.request.ProductDto;
import com.example.vmo1.model.response.ProductResponse;
import com.example.vmo1.repository.ImageRepository;
import com.example.vmo1.repository.ProductRepository;
import com.example.vmo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageRepository imageRepository;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDto save(ProductDto productDto, MultipartFile[] files) {
        // convert dto to entity
        Product productRequest = MapperUtil.map(productDto, Product.class);

        List<Image> lstImg = new ArrayList<>();
//        String filename = null;
        for (MultipartFile file : files) {
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
                Image dbImage = new Image();
                dbImage.setFileName(name);
                dbImage.setProduct(productRequest);
                lstImg.add(dbImage);

            } catch (IOException e) {
                e.printStackTrace();
//                return new UploadFileResponse(null, "Image is not upload success");
            }
        }
        Product product = productRepository.save(productRequest);
        imageRepository.saveAll(lstImg);

        product.setLstImg(lstImg);

        // convert entity to dto
        return MapperUtil.map(product, ProductDto.class);

    }
    @Override
    public ProductDto updateProduct(ProductDto productDto, long id, MultipartFile[] files) {
        Product product = MapperUtil.map(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Find product by id", "Product", id)), Product.class);
        imageRepository.deleteImagesByProduct(product.getId());

        return save(productDto, files);
    }
    @Override
    public ProductResponse getAllProduct(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Product> products = productRepository.findAll(pageable);

        List<Product> productList = products.getContent();
        List<ProductDto> content = productList.stream().map(product -> MapperUtil.map(product, ProductDto.class)).collect(Collectors.toList());

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setPageNo(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLast(products.isLast());

        return productResponse;
    }
    @Override
    public void deleteProduct(long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Find product by id", "Product", id));
        product.setIs_deleted(true);
        productRepository.save(product);
    }

}

