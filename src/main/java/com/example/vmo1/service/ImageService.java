package com.example.vmo1.service;

import com.example.vmo1.model.entity.Image;
import com.example.vmo1.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository;

    public List<Image> saveImage(List<Image> image){
        return imageRepository.saveAll(image);
    }


    public InputStream getResource(String path, String filename) throws FileNotFoundException {
        String fullPath = path + File.separator + filename;
        InputStream is = new FileInputStream(fullPath);

        return is;
    }


}
