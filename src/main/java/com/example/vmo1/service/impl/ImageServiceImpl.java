package com.example.vmo1.service.impl;

import com.example.vmo1.model.entity.Image;
import com.example.vmo1.repository.ImageRepository;
import com.example.vmo1.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;
    @Override
    public List<Image> saveImage(List<Image> image){
        return imageRepository.saveAll(image);
    }

    @Override
    public InputStream getResource(String path, String filename) throws FileNotFoundException {
        String fullPath = path + File.separator + filename;
        InputStream is = new FileInputStream(fullPath);

        return is;
    }


}
