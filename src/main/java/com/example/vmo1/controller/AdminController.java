package com.example.vmo1.controller;


import com.example.vmo1.model.request.SignupRequest;
import com.example.vmo1.model.request.UpdateAccountByAdminRequest;
import com.example.vmo1.model.response.AccountInforResponse;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.model.response.ShopResponse;
import com.example.vmo1.service.AccountService;
import com.example.vmo1.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ShopService shopService;

    @PostMapping("/addAccount")
    public ResponseEntity<?> addAccountByAdmin(@RequestBody SignupRequest request){
        return ResponseEntity.ok(accountService.addAccountByAdmin(request));
    }

    @GetMapping("/account/all")
    public ResponseEntity<?> getAllAccount(@RequestParam(defaultValue = "ROLE_USER") String name, @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        return ResponseEntity.ok(accountService.getAllAccount(name, pageNo, pageSize));
    }

    @GetMapping("/shop/all")
    public ShopResponse getAllShop(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                   @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize){
        return shopService.getAllShop(pageNo, pageSize);
    }

    @PutMapping("/account/update/{id}")
    public ResponseEntity<?> updateAccountByAdmin(@RequestBody UpdateAccountByAdminRequest request,@PathVariable("id") long id){
        AccountInforResponse accountResponse = accountService.updateAccountByAdmin(request, id);
        return ResponseEntity.ok(accountResponse);
    }

    @DeleteMapping("/account/delete/{id}")
    public ResponseEntity<?> deleteAccountByAdmin(@PathVariable("id") long id){
        accountService.deleteAccountByAdmin(id);
        return ResponseEntity.ok(new MessageResponse("Account have been delete!!!"));
    }
}
