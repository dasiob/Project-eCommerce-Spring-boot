package com.example.vmo1.service.impl;


import com.example.vmo1.commons.configs.MapperUtil;
import com.example.vmo1.commons.exeptions.ResourceNotFoundException;
import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.entity.Role;
import com.example.vmo1.model.request.AccountDto;
import com.example.vmo1.model.request.UpdateAccountRequest;
import com.example.vmo1.model.response.AccountResponse;
import com.example.vmo1.repository.AccountRepository;
import com.example.vmo1.repository.RoleRepository;
import com.example.vmo1.model.entity.ConfirmationToken;
import com.example.vmo1.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ConfirmationTokenServiceImpl confirmationTokenServiceImpl;

    @Override
    public String signUpAccount(Account account) {
        boolean emailExists = accountRepository.findByEmail(account.getEmail()).isPresent();
        boolean usernameExists = accountRepository.findByUsername(account.getUsername()).isPresent();
        if (emailExists && usernameExists) {

            Account accountPrevious =  accountRepository.findByEmail(account.getEmail()).get();
            Boolean isEnabled = accountPrevious.isEnabled();

            if (!isEnabled) {
                String token = UUID.randomUUID().toString();
                //A method to save user and token in this class
                saveConfirmationToken(accountPrevious, token);
                return token;
            }
            throw new IllegalStateException(String.format("Username or email %s already exists!", account.getEmail(), account.getUsername()));
        }
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_USER").get());
        account.setRoles(roles);

        //Saving the user after encoding the password
        accountRepository.save(account);

        //Creating a token from UUID
        String token = UUID.randomUUID().toString();

        //Getting the confirmation token and then saving it
        saveConfirmationToken(account, token);

        //Returning token
        return token;
    }

    @Override
    public void saveConfirmationToken(Account account, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), account);
        confirmationTokenServiceImpl.saveConfirmationToken(confirmationToken);
    }


//    public MessageResponse updatePassword(CustomUserDetails customUserDetails, UpdatePasswordRequest updatePasswordRequest){
//        String email = customUserDetails.getEmail();
//        Account currentAccount = accountRepository.findByEmail(email).get();
//        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), currentAccount.getPassword())){
//            return new MessageResponse("No matching account found");
//        }
//        String newPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
//        currentAccount.setPassword(newPassword);
//        accountRepository.save(currentAccount);
//        return new MessageResponse("Update password successfully");
//    }

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
    public UpdateAccountRequest updateProfile(UpdateAccountRequest updateAccountRequest, long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "Update account by id", id));
        account.setFullname(updateAccountRequest.getFullname());
        account.setPhone(account.getPhone());
        accountRepository.save(account);
        return MapperUtil.map(account, UpdateAccountRequest.class);
    }
}
