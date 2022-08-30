package com.example.vmo1.service;

import com.example.vmo1.model.entity.Image;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface ImageService {
    List<Image> saveImage(List<Image> image);
    InputStream getResource(String path, String filename) throws FileNotFoundException;

}
