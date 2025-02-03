package ru.otus.hw.services;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws EntityNotFoundException {
        var user = userRepository.findByLogin(username)
                .orElseThrow(() -> new EntityNotFoundException("User with login: %s has not found".formatted(username)));

        return User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
