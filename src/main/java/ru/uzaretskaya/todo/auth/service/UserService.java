package ru.uzaretskaya.todo.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uzaretskaya.todo.auth.repository.entity.Activity;
import ru.uzaretskaya.todo.auth.repository.entity.Role;
import ru.uzaretskaya.todo.auth.repository.entity.User;
import ru.uzaretskaya.todo.auth.repository.ActivityRepository;
import ru.uzaretskaya.todo.auth.repository.RoleRepository;
import ru.uzaretskaya.todo.auth.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    public static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ActivityRepository activityRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, ActivityRepository activityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.activityRepository = activityRepository;
    }

    public void register(User user, Activity activity) {
        userRepository.save(user);
        activityRepository.save(activity);
    }

    public boolean exists(String username, String email) {
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Optional<Activity> findActivityByUuid(String uuid) {
        return activityRepository.findByUuid(uuid);
    }

    public Optional<Activity> findActivityByUserId(Long id) {
        return activityRepository.findByUserId(id);
    }

    public int activate(String uuid) {
        return activityRepository.changeActivated(uuid, true);
    }

    public int updatePassword(String password, long id) {
        return userRepository.updatePassword(password, id);
    }

    // maybe for future
    public int deactivate(String uuid) {
        return activityRepository.changeActivated(uuid, false);
    }

}
