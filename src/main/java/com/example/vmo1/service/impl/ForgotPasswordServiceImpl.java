package com.example.vmo1.service.impl;

import com.example.vmo1.commons.exceptions.ResourceNotFoundException;
import com.example.vmo1.model.entity.Account;
import com.example.vmo1.model.entity.PasswordResetToken;
import com.example.vmo1.model.request.PasswordResetLinkRequest;
import com.example.vmo1.model.request.PasswordResetRequest;
import com.example.vmo1.model.response.MessageResponse;
import com.example.vmo1.repository.AccountRepository;
import com.example.vmo1.repository.PasswordResetTokenRepository;
import com.example.vmo1.service.EmailSender;
import com.example.vmo1.service.ForgotPasswordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    @Autowired
    private PasswordResetTokenServiceImpl passwordResetTokenServiceImpl;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public String confirmTokenResetPassword(String token) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken.get().getConfirmedAt() != null) {
            throw new IllegalStateException("Email is already confirmed");
        }

        LocalDateTime expiresAt = passwordResetToken.get().getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token is already expired!");
        }

        passwordResetTokenRepository.updateConfirmedAt(token, LocalDateTime.now());

        //Returning confirmation message if the token matches
        return "Your email is confirmed. Please input new password to reset password!!!";
    }
    @Override
    public String sendTokenToChangePassword(PasswordResetLinkRequest request){
        boolean isExist = accountRepository.findByEmail(request.getEmail()).isPresent();
        if(isExist){
            Account account = accountRepository.findByEmail(request.getEmail()).get();
            String token = UUID.randomUUID().toString();
            saveConfirmationToken(account, token);

            String link = "http://localhost:8082/api/v1/auth/forgot-password/confirm?token=" + token;
            emailSender.sendEmail(account.getEmail(), buildEmail(account.getUsername(), link));
            return "Success: Token send successfully!" + token;
        } else {
            return "Fail: Email is not found";
        }
    }
    @Override
    public MessageResponse changePassword(PasswordResetRequest request){
        PasswordResetToken token = passwordResetTokenServiceImpl.getValidToken(request);
        if(StringUtils.isEmpty(request.getEmail())){
            return new MessageResponse("A new password is required");
        }
        if(request.getToken().equals(token.getToken())){
            updatePassword(request.getEmail(), request.getPassword());
            passwordResetTokenServiceImpl.inIsActiveToken(request);

            return new MessageResponse("Change password successfully!!");
        }
        return new MessageResponse("Change password fail!!");
    }
    @Override
    public boolean updatePassword(String email, String password){
        Account account = accountRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Find email","email", email ));
        String encodedPassword = passwordEncoder.encode(password);
        account.setPassword(encodedPassword);
        accountRepository.save(account);
        return true;
    }
    @Override
    public void saveConfirmationToken(Account account, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, LocalDateTime.now().plusMinutes(15), true,account);
        passwordResetTokenRepository.save(passwordResetToken);
    }
    @Override
    public String buildEmail(String name, String link){
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
}
