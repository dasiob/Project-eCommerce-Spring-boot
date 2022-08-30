package com.example.vmo1.controller;


import com.example.vmo1.model.request.UpdateAccountRequest;
import com.example.vmo1.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;

//    @PostMapping("/password/update")
//    public ResponseEntity<?> updateAccountPassword(CustomUserDetails customUserDetails, @RequestBody UpdatePasswordRequest updatePasswordRequest){
//        return ResponseEntity.ok(accountService.updatePassword(customUserDetails, updatePasswordRequest));
//    }

    @GetMapping
    public ResponseEntity<?> getAllAccount(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        return ResponseEntity.ok(accountService.getAllAccount(pageNo, pageSize));
    }

    @PutMapping("/profile/update/{id}")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateAccountRequest updateAccountRequest, @PathVariable("id") long id){
        UpdateAccountRequest accountResponse = accountService.updateProfile(updateAccountRequest, id);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }


}
