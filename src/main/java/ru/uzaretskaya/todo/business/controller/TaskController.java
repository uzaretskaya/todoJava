package ru.uzaretskaya.todo.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uzaretskaya.todo.business.entity.Task;
import ru.uzaretskaya.todo.business.service.TaskService;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;
import static ru.uzaretskaya.todo.business.util.AllExecutedMethodsLogger.loggingMethodName;

@RestController
@RequestMapping("/task")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Task>> findAll(@RequestBody String email) {
        loggingMethodName(format("TaskController: findAll(%s)", email));
        return ok(taskService.findAll(email));
    }

    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {
        loggingMethodName(format("TaskController: add(%s)", task));


        if(task.getId() != null && task.getId() != 0) {
            return new ResponseEntity("Redundant parameter: ID must be NULL", NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("Missing parameter: title", NOT_ACCEPTABLE);
        }

        return ok(taskService.add(task));
    }

    @PatchMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {
        loggingMethodName(format("CategoryController: update(%s)", task));

        if(task.getId() == null || task.getId() == 0) {
            return new ResponseEntity("Missing parameter: id", NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("Missing parameter: title", NOT_ACCEPTABLE);
        }

        taskService.update(task);

        return new ResponseEntity(OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Task> delete(@RequestBody Long id) {
        loggingMethodName(format("CategoryController: delete(%d)", id));

        if(id == null || id == 0) {
            return new ResponseEntity("Missing parameter: id", NOT_ACCEPTABLE);
        }

        try {
            taskService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity(format("Task id=%d not found!", id), NOT_ACCEPTABLE);
        }

        return new ResponseEntity(OK);
    }

    @PostMapping("/id")
    public ResponseEntity<Task> findById(@RequestBody Long id) {
        loggingMethodName(format("CategoryController: findById(%d)", id));

        Task task;
        try {
            task = taskService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity(format("Category id=%d not found!", id), NOT_ACCEPTABLE);
        }
        return ok(task);
    }
}
