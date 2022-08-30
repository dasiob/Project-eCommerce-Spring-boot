package com.example.vmo1.model.response;

import com.example.vmo1.model.request.AccountDto;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private List<AccountDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

}
