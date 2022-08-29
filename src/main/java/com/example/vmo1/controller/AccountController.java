package com.example.vmo1.controller;


import com.example.vmo1.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

//    @PostMapping("/password/update")
//    public ResponseEntity<?> updateAccountPassword(CustomUserDetails customUserDetails, @RequestBody UpdatePasswordRequest updatePasswordRequest){
//        return ResponseEntity.ok(accountService.updatePassword(customUserDetails, updatePasswordRequest));
//    }
}
