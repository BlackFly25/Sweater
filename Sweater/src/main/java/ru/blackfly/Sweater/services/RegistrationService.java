package ru.blackfly.Sweater.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.blackfly.Sweater.models.User;
import ru.blackfly.Sweater.repos.UserRepo;

@Service
public class RegistrationService {
    private final UserRepo userRepository;

    @Autowired
    public RegistrationService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void registerUser(User user) {
        userRepository.save(user);
    }
}
