package ru.uzaretskaya.todo.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.uzaretskaya.todo.auth.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select case when count (u) > 0 then true else false end " +
            "from User u where lower(u.email) = lower(:email)")
    boolean existsByEmail(@Param("email") String email);

    @Query("select case when count (u) > 0 then true else false end " +
            "from User u where lower(u.username) = lower(:username)")
    boolean existsByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
