package com.example.vmo1.validation.validator;


import com.example.vmo1.model.request.PasswordResetRequest;
import com.example.vmo1.validation.annotation.MatchPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchPasswordValidator implements ConstraintValidator<MatchPassword, PasswordResetRequest> {

    @Override
    public void initialize(MatchPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(PasswordResetRequest value, ConstraintValidatorContext context) {
        String password = value.getPassword();
        String confirmPassword = value.getConfirmPassword();
        if(password == null || !password.equals(confirmPassword)) {
            return false;
        }
        return true;
    }
}
