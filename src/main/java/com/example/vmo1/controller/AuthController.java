package com.example.vmo1.controller;

import com.example.vmo1.model.request.LoginRequest;
import com.example.vmo1.model.request.PasswordResetLinkRequest;
import com.example.vmo1.model.request.PasswordResetRequest;
import com.example.vmo1.model.request.SignupRequest;
import com.example.vmo1.model.response.JWTAuthResponse;
import com.example.vmo1.repository.AccountRepository;
import com.example.vmo1.security.jwt.JwtTokenProvider;
import com.example.vmo1.service.impl.AccountServiceImpl;
import com.example.vmo1.service.impl.ForgotPasswordServiceImpl;
import com.example.vmo1.service.impl.RegistrationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private RegistrationServiceImpl registrationService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    private ForgotPasswordServiceImpl forgotPasswordService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthResponse(token));

    }

    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody SignupRequest request) {
        // add check for username exists in a DB
        if(accountRepository.existsByUsername(request.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // add check for email exists in DB
        if(accountRepository.existsByEmail(request.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(registrationService.register(request));

    }

    @GetMapping(path = "/registration/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @GetMapping(path = "/forgot-password/confirm")
    public String confirmTokenToChangePassword(@RequestParam("token") String token) {
        return forgotPasswordService.confirmTokenResetPassword(token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendTokenToChangePassword(@RequestBody PasswordResetLinkRequest request) {
        return ResponseEntity.ok(forgotPasswordService.sendTokenToChangePassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request){
        return ResponseEntity.ok(forgotPasswordService.changePassword(request));
    }
}
