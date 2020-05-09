package io.agileintelligence.ppmt.validator;

import io.agileintelligence.ppmt.domain.UserBO;
import io.agileintelligence.ppmt.payload.SignUpRequest;
import io.agileintelligence.ppmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SignUpValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserBO.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        SignUpRequest newUser = (SignUpRequest) object;

        if (newUser.getPassword().length() < 6) {
            errors.rejectValue("password", "Length", "Password must be at least 6 characters");
        }

        if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Match", "Passwords must match");
        }

        if (userRepository.existsByUsername(newUser.getUsername())) {
            errors.rejectValue("username", "exists", "Username is already taken!");
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            errors.rejectValue("email", "exists", "Email Address already in use!");
        }
        //confirmPassword
    }
}
