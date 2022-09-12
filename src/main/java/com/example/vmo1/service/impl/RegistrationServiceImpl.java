package com.example.vmo1.service.impl;


import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.entity.Role;
import com.example.vmo1.model.request.SignupRequest;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.repository.AccountRepository;
import com.example.vmo1.repository.ConfirmationTokenRepository;
import com.example.vmo1.repository.RoleRepository;
import com.example.vmo1.service.EmailSender;
import com.example.vmo1.service.RegistrationService;
import com.example.vmo1.model.entity.ConfirmationToken;
import com.example.vmo1.validation.validator.EmailValidator;
import com.example.vmo1.validation.validator.PhoneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private PhoneValidator phoneValidator;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public MessageResponse register(SignupRequest request) {
//        Account account = accountRepository.findByUsername(request.getUsername()).orElse(null);
        boolean emailExists = accountRepository.findByEmail(request.getEmail()).isPresent();
        boolean usernameExists = accountRepository.findByUsername(request.getUsername()).isPresent();
        if (emailExists || usernameExists) {
            return new MessageResponse("Fail: username or email already use");
        }
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (isValidEmail) {
            Account accountRegister = new Account();
            if (request.getPhone() != null) {
                boolean isValidPhone = phoneValidator.test(request.getPhone());
                if (!isValidPhone) {
                    return new MessageResponse("Phone number is not valid");
                } else {
                    accountRegister.setPhone(request.getPhone());
                }
            }

            accountRegister.setFullname(request.getFullname());
            accountRegister.setUsername(request.getUsername());
            accountRegister.setPassword(passwordEncoder.encode(request.getPassword()));
            accountRegister.setEmail(request.getEmail());
            accountRegister.setEnable(false);

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName("ROLE_USER").get());
            accountRegister.setRoles(roles);
            accountRepository.save(accountRegister);

            String tokenForNewAccount = UUID.randomUUID().toString();
            saveConfirmationToken(accountRegister, tokenForNewAccount);
            //Since, we are running the spring boot application in localhost, we are hardcoding the
            //url of the server. We are creating a POST request with token param
            String link = "http://localhost:8082/api/v1/auth/registration/confirm?token=" + tokenForNewAccount;
            boolean isSuccess = emailSender.sendEmail(request.getEmail(), buildEmail(request.getUsername(), link));
            if(isSuccess){
                return new MessageResponse("Success: Token send successfully!");
            } else {
                return new MessageResponse("Fail: Can not send email");
            }
        } else {
            throw new IllegalStateException(String.format("Email %s, not valid", request.getEmail()));
        }
    }

    @Override
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmToken = confirmationTokenRepository.findByToken(token).orElse(null);
        if (confirmToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email is already confirmed");
        }

        LocalDateTime expiresAt = confirmToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token is already expired!");
        }

        confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
        accountRepository.enableAccount(confirmToken.getAccount().getEmail());

        //Returning confirmation message if the token matches
        return "Your email is confirmed. Thank you for using our service!";
    }

    @Override
    public void saveConfirmationToken(Account account, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now().plusMinutes(15), account);
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public String buildEmail(String name, String link) {
        return "<div style=\"display: none; font-size: 1px; line-height: 1px; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; font-family: sans-serif;\">\n" +
                "    &nbsp;\n" +
                "</div>\n" +
                "<table style=\"background: #F7F8FA; border: 0; border-radius: 0; width: 100%;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "    <td class=\"tw-body\" style=\"padding: 15px 15px 0;\" align=\"center\">\n" +
                "        <table style=\"background: #F7F8FA; border: 0; border-radius: 0;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "            <tbody>\n" +
                "            <tr>\n" +
                "                <td class=\"\" style=\"width: 600px;\" align=\"center\">\n" +
                "                    <p style=\"padding: 5px 5px 5px; font-size: 13px; margin: 0 0 0px; color: #316fea;\" align=\"right\">\n" +
                "                    </p>\n" +
                "                    <table style=\"background: #ffffff; border: 0px; border-radius: 4px; width: 99.6672%; overflow: hidden;\"\n" +
                "                           cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                        <tbody>\n" +
                "                        <tr>\n" +
                "                            <td class=\"\" style=\"padding: 0px; width: 100%;\" align=\"center\">\n" +
                "                                <table style=\"background: #336f85; border: 0px; border-radius: 0px; width: 599px; height: 53px; margin-left: auto; margin-right: auto;\"\n" +
                "                                       cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                    <tbody>\n" +
                "                                    <tr>\n" +
                "\n" +
                "                                        <td class=\"tw-card-header\"\n" +
                "                                            style=\"padding: 5px 5px px; width: 366px; color: #ffff; text-decoration: none; font-family: sans-serif;\"\n" +
                "                                            align=\"center\"><span\n" +
                "                                                style=\"font-weight: 600;\">Email Confirmation Required</span></td>\n" +
                "\n" +
                "                                    </tr>\n" +
                "                                    </tbody>\n" +
                "                                </table>\n" +
                "                                <p><br/><br/></p>\n" +
                "                                <table dir=\"ltr\" style=\"border: 0; width: 100%;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                    <tbody>\n" +
                "                                    <tr>\n" +
                "                                        <td class=\"tw-card-body\"\n" +
                "                                            style=\"padding: 20px 35px; text-align: left; color: #6f6f6f; font-family: sans-serif; border-top: 0;\">\n" +
                "                                            <h1 class=\"tw-h1\"\n" +
                "                                                style=\"font-size: 24px; font-weight: bold; mso-line-height-rule: exactly; line-height: 32px; margin: 0 0 20px; color: #474747;\">\n" +
                "                                                Hello " + name + ",</h1>\n" +
                "                                            <p class=\"\"\n" +
                "                                               style=\"margin: 20px 0; font-size: 16px; mso-line-height-rule: exactly; line-height: 24px;\">\n" +
                "                                            <span style=\"font-weight: 400;\">Thank you for joining <strong>Customer Engagement App</strong>, an efficient and smart solution to manage your health!</span><br/><br/><span\n" +
                "                                                style=\"font-weight: 400;\">To complete the registration process, please confirm your email address to activate your account</span>\n" +
                "                                            <table style=\"border: 0; width: 100%;\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                <tbody>\n" +
                "                                                <tr>\n" +
                "                                                    <td>\n" +
                "                                                        <table class=\"button mobile-w-full\"\n" +
                "                                                               style=\"border: 0px; border-radius: 7px; margin: 0px auto; width: 525px; background-color: #008bcb; height: 50px;\"\n" +
                "                                                               cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
                "                                                            <tbody>\n" +
                "                                                            <tr>\n" +
                "                                                                <td class=\"button__td \"\n" +
                "                                                                    style=\"border-radius: 7px; text-align: center; width: 523px;\"><!-- [if mso]>\n" +
                "                                                                    <a href=\"\" class=\"button__a\" target=\"_blank\"\n" +
                "                                                                       style=\"border-radius: 4px; color: #FFFFFF; display: block; font-family: sans-serif; font-size: 18px; font-weight: bold; mso-height-rule: exactly; line-height: 1.1; padding: 14px 18px; text-decoration: none; text-transform: none; border: 1px solid #316FEA;\"> </a>\n" +
                "                                                                    <![endif]--> <!-- [if !mso]><!--> <a\n" +
                "                                                                            class=\"button__a\"\n" +
                "                                                                            style=\"border-radius: 4px; color: #ffffff; display: block; font-family: sans-serif; font-size: 18px; font-weight: bold; mso-height-rule: exactly; line-height: 1.1; padding: 14px 18px; text-decoration: none; text-transform: none; border: 0;\"\n" +
                "                                                                            href=\"" + link + "\"\n" +
                "                                                                            target=\"_blank\"\n" +
                "                                                                            rel=\"noopener\">Confirm\n" +
                "                                                                        email</a> <!--![endif]--></td>\n" +
                "                                                            </tr>\n" +
                "                                                            </tbody>\n" +
                "                                                        </table>\n" +
                "                                                    </td>\n" +
                "                                                </tr>\n" +
                "                                                </tbody>\n" +
                "                                            </table>\n" +
                "                                            <div class=\"\"\n" +
                "                                                 style=\"border-top: 0; font-size: 1px; mso-line-height-rule: exactly; line-height: 1px; max-height: 0; margin: 20px 0; overflow: hidden;\">\n" +
                "                                                \u200B\n" +
                "                                            </div>\n" +
                "                                            <p class=\"\"\n" +
                "                                               style=\"margin: 20px 0; font-size: 16px; mso-line-height-rule: exactly; line-height: 24px;\">\n" +
                "                                                Contact our support team if you have any questions or concerns.&nbsp;<a\n" +
                "                                                    style=\"color: #316fea; text-decoration: none;\"\n" +
                "                                                    href=\"javascript:void(0);\" target=\"_blank\" rel=\"noopener\">Ask us any\n" +
                "                                                question</a></p>\n" +
                "                                            <p class=\"tw-signoff\"\n" +
                "                                               style=\"margin: 45px 0 5px; font-size: 16px; mso-line-height-rule: exactly; line-height: 24px;\">\n" +
                "                                                Our best, <br/> The Customer Engagement App team</p>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                    </tbody>\n" +
                "                                </table>\n" +
                "                            </td>\n" +
                "                        </tr>\n" +
                "                        </tbody>\n" +
                "                    </table>";
    }

//    @Override
//    public String signUpAccount(Account account) {
//        boolean emailExists = accountRepository.findByEmail(account.getEmail()).isPresent();
//        boolean usernameExists = accountRepository.findByUsername(account.getUsername()).isPresent();
//        if (emailExists && usernameExists) {
////            Account accountPrevious =  accountRepository.findByEmail(account.getEmail()).get();
////            Boolean isEnabled = accountPrevious.isEnabled();
////
////            if (!isEnabled) {
////                String token = UUID.randomUUID().toString();
////                //A method to save user and token in this class
////                saveConfirmationToken(accountPrevious, token);
////                return token;
////            }
//
//        }
//        String encodedPassword = passwordEncoder.encode(account.getPassword());
//        account.setPassword(encodedPassword);
//
//        Set<Role> roles = new HashSet<>();
//        roles.add(roleRepository.findByName("ROLE_USER").get());
//        account.setRoles(roles);
//
//        //Saving the user after encoding the password
//        accountRepository.save(account);
//
//        //Creating a token from UUID
//        String token = UUID.randomUUID().toString();
//
//        //Getting the confirmation token and then saving it
//        saveConfirmationToken(account, token);
//
//        //Returning token
//        return token;
//    }
}
