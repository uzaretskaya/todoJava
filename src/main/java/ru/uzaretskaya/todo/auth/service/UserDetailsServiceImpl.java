package ru.uzaretskaya.todo.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uzaretskaya.todo.auth.entity.User;
import ru.uzaretskaya.todo.auth.repository.UserRepository;

import java.util.Optional;

import static java.lang.String.format;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if(!userOptional.isPresent()) {
            userRepository.findByEmail(username);
        }

        if(!userOptional.isPresent()) {
            throw new UsernameNotFoundException(format("User not found with email or username: %s", username));
        }

        return new UserDetailsImpl(userOptional.get());
    }
}
