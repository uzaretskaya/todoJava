package ru.uzaretskaya.todo.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uzaretskaya.todo.business.repository.entity.Task;
import ru.uzaretskaya.todo.business.repository.search.TaskSearchValues;
import ru.uzaretskaya.todo.business.service.TaskService;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;
import static ru.uzaretskaya.todo.baseUtils.CommonValues.ID_COLUMN;
import static ru.uzaretskaya.todo.baseUtils.CommonValues.PAGE_NUMBER;
import static ru.uzaretskaya.todo.baseUtils.CommonValues.PAGE_SIZE;
import static ru.uzaretskaya.todo.baseUtils.AllExecutedMethodsLogger.loggingMethodName;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

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
        loggingMethodName(format("TaskController: update(%s)", task));

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
        loggingMethodName(format("TaskController: delete(%d)", id));

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
        loggingMethodName(format("TaskController: findById(%d)", id));

        Task task;
        try {
            task = taskService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity(format("Task id=%d not found!", id), NOT_ACCEPTABLE);
        }
        return ok(task);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues) {
        loggingMethodName(format("TaskController: search(%s)", taskSearchValues));

        PageRequest pageRequest;

        if (taskSearchValues == null) {
            taskSearchValues = new TaskSearchValues();

            Sort sort = Sort.by(ASC, ID_COLUMN);
            pageRequest = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, sort);

        } else {
            if(taskSearchValues.getDateFrom() != null) {
                Calendar calendarFrom = Calendar.getInstance();
                calendarFrom.setTime(taskSearchValues.getDateFrom());
                calendarFrom.set(HOUR_OF_DAY, 0);
                calendarFrom.set(MINUTE, 0);
                calendarFrom.set(SECOND, 0);
                calendarFrom.set(MILLISECOND, 0);

                taskSearchValues.setDateFrom(calendarFrom.getTime());
            }

            if(taskSearchValues.getDateTo() != null) {
                Calendar calendarTo = Calendar.getInstance();
                calendarTo.setTime(taskSearchValues.getDateTo());
                calendarTo.set(HOUR_OF_DAY, 23);
                calendarTo.set(MINUTE, 59);
                calendarTo.set(SECOND, 59);
                calendarTo.set(MILLISECOND, 999);

                taskSearchValues.setDateTo(calendarTo.getTime());
            }

            int pageNumber = taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber() : PAGE_NUMBER;
            int pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : PAGE_SIZE;

            String sortDirection = taskSearchValues.getSortDirection();
            Direction direction = sortDirection == null ||
                    sortDirection.trim().length() == 0 ||
                    sortDirection.trim().equals("asc") ? ASC : DESC;

            Sort sort;
            if (taskSearchValues.getSortColumn() != null) {
                sort = Sort.by(direction, taskSearchValues.getSortColumn(), ID_COLUMN);
            } else {
                sort = Sort.by(direction, ID_COLUMN);
            }
            pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        }

        Page<Task> result = taskService.find(taskSearchValues, pageRequest);
        return ok(result);
    }
}
