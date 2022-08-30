package com.example.vmo1.service;

import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.request.UpdateAccountRequest;
import com.example.vmo1.model.response.AccountResponse;

public interface AccountService {
    String signUpAccount(Account account);
    void saveConfirmationToken(Account account, String token);
    AccountResponse getAllAccount(int pageNo, int pageSize);
    UpdateAccountRequest updateProfile(UpdateAccountRequest updateAccountRequest, long id);
}
