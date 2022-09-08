package com.example.vmo1.model.response;

import com.example.vmo1.model.request.ShopDto;
import lombok.Data;

import java.util.List;

@Data
public class ShopResponse {
    private List<ShopDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
