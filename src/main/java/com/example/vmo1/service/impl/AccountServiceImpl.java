package com.example.vmo1.service.impl;


import com.example.vmo1.commons.configs.MapperUtil;
import com.example.vmo1.commons.exceptions.ResourceNotFoundException;
import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.entity.Role;
import com.example.vmo1.model.request.*;
import com.example.vmo1.model.response.AccountInforResponse;
import com.example.vmo1.model.response.AccountResponse;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.repository.AccountRepository;
import com.example.vmo1.repository.RoleRepository;
import com.example.vmo1.security.service.CustomUserDetails;
import com.example.vmo1.service.AccountService;
import com.example.vmo1.validation.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public MessageResponse addAccountByAdmin(SignupRequest request) {
        Account account = accountRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).get();

        if(account != null){
            return new MessageResponse("Fail: username or email already use");
        }
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (isValidEmail) {
            Account accountRegister = new Account();
            accountRegister.setUsername(request.getUsername());
            accountRegister.setPassword(passwordEncoder.encode(request.getPassword()));
            accountRegister.setEmail(request.getEmail());
            accountRegister.setEnable(true);

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_USER").get());
            accountRegister.setRoles(roles);
            return new MessageResponse("Success: Register successfully!");
        } else {
            throw new IllegalStateException(String.format("Email %s, not valid", request.getEmail()));
        }
    }
    @Override
    public MessageResponse updatePassword(CustomUserDetails customUserDetails, UpdatePasswordRequest updatePasswordRequest){
        String email = customUserDetails.getEmail();
        Account currentAccount = accountRepository.findByEmail(email).get();
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), currentAccount.getPassword())){
            return new MessageResponse("No matching account found");
        }
        if(updatePasswordRequest.getOldPassword().equals(updatePasswordRequest.getNewPassword())){
            return new MessageResponse("New password must different old password");
        }
        String newPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        currentAccount.setPassword(newPassword);
        accountRepository.save(currentAccount);
        return new MessageResponse("Update password successfully");
    }

    @Override
    public AccountResponse getAllAccount(int pageNo,int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Account> accounts = accountRepository.findAll(pageable);

        List<Account> accountList = accounts.getContent();
        List<AccountDto> content = accountList.stream().map(account -> MapperUtil.map(account, AccountDto.class)).collect(Collectors.toList());

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setContent(content);
        accountResponse.setPageNo(accounts.getNumber());
        accountResponse.setPageSize(accounts.getSize());
        accountResponse.setTotalElements(accounts.getTotalElements());
        accountResponse.setTotalPages(accounts.getTotalPages());
        accountResponse.setLast(accounts.isLast());

        return accountResponse;
    }
    @Override
    public AccountInforResponse updateProfile(UpdateAccountRequest request, long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Update account by id", id));
        account.setFullname(request.getFullname());
        account.setPhone(request.getPhone());
        accountRepository.save(account);
        return MapperUtil.map(account, AccountInforResponse.class);
    }

    @Override
    public AccountInforResponse updateAccountByAdmin(UpdateAccountByAdminRequest request, long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Update account by id", id));
        account.setEnable(request.getEnable());
        accountRepository.save(account);
        return MapperUtil.map(account, AccountInforResponse.class);
    }

    @Override
    public void deleteAccountByAdmin(long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Delete account by admin", id));
        account.setIs_deleted(true);
        accountRepository.save(account);
    }


}
