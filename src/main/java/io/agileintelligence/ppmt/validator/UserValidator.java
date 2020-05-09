package io.agileintelligence.ppmt.validator;

import io.agileintelligence.ppmt.domain.UserBO;
import io.agileintelligence.ppmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserBO.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        UserBO userBO = (UserBO) object;

        if (userBO == null) {
            errors.rejectValue("userBO", "exists", "userBO does not exists");
        }
    }
}
