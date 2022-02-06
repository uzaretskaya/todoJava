package ru.uzaretskaya.todo.auth.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.uzaretskaya.todo.auth.repository.entity.Activity;

import java.util.Optional;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {

    @Modifying
    @Transactional
    @Query("update Activity a set a.activated = :active where a.uuid = :uuid")
    int changeActivated(@Param("uuid") String uuid, @Param("active") boolean active);

    Optional<Activity> findByUserId(Long id);

    Optional<Activity> findByUuid(String uuid);
}
