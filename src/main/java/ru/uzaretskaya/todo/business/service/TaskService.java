package ru.uzaretskaya.todo.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.uzaretskaya.todo.business.repository.entity.Task;
import ru.uzaretskaya.todo.business.repository.TaskRepository;
import ru.uzaretskaya.todo.business.repository.search.TaskSearchValues;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(String email) {
        return taskRepository.findByUserEmailOrderByTitleAsc(email);
    }

    public Task add(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).get();
    }

    public Page<Task> find(TaskSearchValues taskSearchValues, PageRequest pageRequest) {
        return taskRepository.findByParams(
                taskSearchValues.getTitle(),
                taskSearchValues.getCompleted(),
                taskSearchValues.getPriorityId(),
                taskSearchValues.getCategoryId(),
                taskSearchValues.getEmail(),
                taskSearchValues.getDateFrom(),
                taskSearchValues.getDateTo(),
                pageRequest);
    }
}
