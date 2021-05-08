package ru.uzaretskaya.todo.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.uzaretskaya.todo.business.entity.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserEmailOrderByTitleAsc(String email);
}
