package ru.uzaretskaya.todo.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.uzaretskaya.todo.auth.entity.User;
import ru.uzaretskaya.todo.auth.repository.UserRepository;

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public User save(User user) {
        return repository.save(user);
    }
}
