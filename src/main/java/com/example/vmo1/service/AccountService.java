package com.example.vmo1.service;

import com.example.vmo1.model.request.SignupRequest;
import com.example.vmo1.model.request.UpdateAccountByAdminRequest;
import com.example.vmo1.model.request.UpdateAccountRequest;
import com.example.vmo1.model.request.UpdatePasswordRequest;
import com.example.vmo1.model.response.AccountInforResponse;
import com.example.vmo1.model.response.AccountResponse;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.security.service.CustomUserDetails;

public interface AccountService {
    MessageResponse addAccountByAdmin(SignupRequest request);
    AccountResponse getAllAccount(int pageNo, int pageSize);
    AccountInforResponse updateProfile(UpdateAccountRequest request, long id);
    AccountInforResponse updateAccountByAdmin(UpdateAccountByAdminRequest request, long id);
    void deleteAccountByAdmin(long id);

    MessageResponse updatePassword(CustomUserDetails customUserDetails, UpdatePasswordRequest updatePasswordRequest);
}
