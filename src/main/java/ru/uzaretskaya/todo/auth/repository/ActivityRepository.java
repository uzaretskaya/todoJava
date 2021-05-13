package ru.uzaretskaya.todo.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.uzaretskaya.todo.auth.entity.Activity;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
