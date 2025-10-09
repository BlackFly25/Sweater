package ru.blackfly.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.blackfly.services.PersonDetailsService;
import ru.blackfly.models.User;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;

        // Проверка уникальности имени пользователя через сервис
        try {
            personDetailsService.loadUserByUsername(user.getUsername());
            }
         catch (UsernameNotFoundException ignored){
        return;// Пользователь не найден — всё ок
        }
        errors.rejectValue("username", "", "Username is already taken");

    }

}



