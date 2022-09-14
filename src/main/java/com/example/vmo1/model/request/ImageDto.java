package com.example.vmo1.model.request;

import com.example.vmo1.validation.annotation.ValidFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private Long id;
    @ValidFile
    private String name;
}
