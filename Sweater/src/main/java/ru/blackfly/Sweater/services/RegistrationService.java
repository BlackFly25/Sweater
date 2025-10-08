package ru.blackfly.Sweater.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.blackfly.Sweater.models.User;
import ru.blackfly.Sweater.repos.UserRepo;

@Service
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;//внедряем для шифрования паролей
    private final UserRepo userRepository;

    @Autowired
    public RegistrationService(PasswordEncoder passwordEncoder, UserRepo userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     *
     * Ниже метод для шифрования пароля, но нужно не забыть, при аутентификации сравнивать шифрованные пароли.
     * Если забыть поставить сравнение - никто не зайдет на сайт.
     */
    @Transactional
    public void registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());//метод зашифрует пароль
        user.setPassword(encodedPassword);//сохраняем шифрованный пароль в БД, вместо того что ввели в форму
        userRepository.save(user);
    }
}
