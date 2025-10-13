package ru.blackfly.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.blackfly.models.User;
import ru.blackfly.repos.UserRepo;
import ru.blackfly.security.PersonDetails;

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
    @Transactional
    public void update(int id,User userUp) {
        userUp.setId(id);
        userRepository.save(userUp);
    }
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}