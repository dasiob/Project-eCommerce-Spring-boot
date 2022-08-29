package com.example.vmo1.service;

import com.example.vmo1.commons.configs.MapperUtil;
import com.example.vmo1.model.entity.Image;
import com.example.vmo1.model.entity.Product;
import com.example.vmo1.model.request.ProductDto;
import com.example.vmo1.model.response.ProductResponse;
import com.example.vmo1.repository.ImageRepository;
import com.example.vmo1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageRepository imageRepository;

    @Value("${project.image}")
    private String path;


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

    public ProductDto updateProduct(ProductDto productDto, long id, MultipartFile[] files) {
        Product product = productRepository.findById(id).orElse(null);

        List<Image> lstImg = product.getLstImg();
        System.out.println(lstImg);
        imageRepository.deleteAll(lstImg);
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
                dbImage.setProduct(product);
                lstImg.add(dbImage);

            } catch (IOException e) {
                e.printStackTrace();
//                return new UploadFileResponse(null, "Image is not upload success");
            }
        }
        imageRepository.saveAll(lstImg);

        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setCategory(productDto.getCategory());

        Product updateProduct = productRepository.save(product);
        return MapperUtil.map(updateProduct, ProductDto.class);
    }

    public ProductResponse getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir){

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

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

//    private ProductDto mapToDto(Product product){
//        return  MapperUtil.map(product, ProductDto.class)
//    }
//
//    private Product mapToEntity(ProductDto productDto){
//        return MapperUtil.map(productDto, Product.class);
//    }
}

