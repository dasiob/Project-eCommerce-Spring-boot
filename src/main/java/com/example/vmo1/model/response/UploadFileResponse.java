package com.example.vmo1.model.response;

import lombok.Data;

@Data
public class UploadFileResponse {
    private String fileName;
    private String message;


    public UploadFileResponse(String fileName, String message) {
        this.fileName = fileName;
        this.message = message;
    }
}
