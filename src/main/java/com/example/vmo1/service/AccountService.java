package com.example.vmo1.service;


import com.example.vmo1.email.EmailSender;
import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.entity.Role;
import com.example.vmo1.model.request.UpdatePasswordRequest;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.repository.AccountRepository;
import com.example.vmo1.repository.RoleRepository;
import com.example.vmo1.security.service.CustomUserDetails;
import com.example.vmo1.security.token.ConfirmationToken;
import com.example.vmo1.security.token.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailSender emailSender;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));
    }


    public String signUpAccount(Account account) {
        boolean userExists = accountRepository.findByEmail(account.getEmail()).isPresent();
        if (userExists) {

            Account accountPrevious =  accountRepository.findByEmail(account.getEmail()).get();
            Boolean isEnabled = accountPrevious.isEnabled();

            if (!isEnabled) {
                String token = UUID.randomUUID().toString();
                //A method to save user and token in this class
                saveConfirmationToken(accountPrevious, token);
                return token;
            }
            throw new IllegalStateException(String.format("User with email %s already exists!", account.getEmail()));
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


    private void saveConfirmationToken(Account account, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), account);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
    }

    public int enableAccount(String email) {
        return accountRepository.enableAccount(email);

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



}
