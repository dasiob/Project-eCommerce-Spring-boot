package com.example.vmo1.model.response;

import com.example.vmo1.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Getter
@Setter
public class AccountInfoResponse {
    private int id;
    private String username;
    private String email;
    private Set<Role> role;
}
