package ru.blackfly.Sweater.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.blackfly.Sweater.models.User;
import ru.blackfly.Sweater.repos.UserRepo;
import ru.blackfly.Sweater.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    @Autowired
    public PersonDetailsService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user= userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(user.get());
    }
}