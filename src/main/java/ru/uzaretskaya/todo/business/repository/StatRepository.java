package ru.uzaretskaya.todo.business.repository;

import org.springframework.data.repository.CrudRepository;
import ru.uzaretskaya.todo.business.repository.entity.Stat;

public interface StatRepository extends CrudRepository<Stat, Long> {
    Stat findByUserEmail(String email);
}
