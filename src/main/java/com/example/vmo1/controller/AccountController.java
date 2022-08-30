package com.example.vmo1.controller;


import com.example.vmo1.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<?> getAllAccount(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        return ResponseEntity.ok(accountService.getAllAccount(pageNo, pageSize));
    }
}
