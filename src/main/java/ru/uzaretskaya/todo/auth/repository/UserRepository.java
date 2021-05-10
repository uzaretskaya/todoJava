package ru.uzaretskaya.todo.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.uzaretskaya.todo.auth.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
