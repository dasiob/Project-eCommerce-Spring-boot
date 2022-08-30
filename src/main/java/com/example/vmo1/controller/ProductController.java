package com.example.vmo1.controller;

import com.example.vmo1.model.request.ProductDto;
import com.example.vmo1.model.response.ProductResponse;
import com.example.vmo1.repository.ImageRepository;
import com.example.vmo1.service.ImageService;
import com.example.vmo1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ImageService imageService;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ProductService productService;
    @Value("${project.image}")
    private String path;


    @PostMapping("/add")
    public ResponseEntity<?> uploadMultipleFiles(@RequestPart("files") MultipartFile[] files,@RequestPart ProductDto productDto) {
        return ResponseEntity.ok(productService.save(productDto, files));
    }

    @GetMapping(value = "/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = this.imageService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@RequestPart ProductDto productDto, @PathVariable(name= "id") long id, MultipartFile[] files){
        ProductDto productResponse = productService.updateProduct(productDto, id, files);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping
    public ProductResponse getAllProducts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
            return productService.getAllProduct(pageNo, pageSize);
    }

    //    @PostMapping("/upload")
//    public ResponseEntity<UploadFileResponse> fileUpload(@RequestParam("file") MultipartFile file, @RequestBody Product product) {
//        String filename = null;
//        try{
//             filename = imageService.upload(file, path);
//
//
//        } catch (IOException e){
//            e.printStackTrace();
//            return new ResponseEntity<>(new UploadFileResponse(null, "Image is not upload success"), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return new ResponseEntity<>(new UploadFileResponse(filename, "Image is success"), HttpStatus.OK);
//    }
}
