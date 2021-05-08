package ru.uzaretskaya.todo.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uzaretskaya.todo.business.entity.Priority;
import ru.uzaretskaya.todo.business.search.PrioritySearchValues;
import ru.uzaretskaya.todo.business.service.PriorityService;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;
import static ru.uzaretskaya.todo.business.util.AllExecutedMethodsLogger.loggingMethodName;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private PriorityService priorityService;

    @Autowired
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Priority>> findAll(@RequestBody String email) {
        loggingMethodName(format("PriorityController: findAll(%s)", email));
        return ok(priorityService.findAll(email));
    }

    @PutMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {
        loggingMethodName(format("PriorityController: add(%s)", priority));

        if(priority.getId() != null && priority.getId() != 0) {
            return new ResponseEntity("Redundant parameter: ID must be NULL", NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("Missing parameter: title", NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("Missing parameter: color", NOT_ACCEPTABLE);
        }

        return ok(priorityService.add(priority));
    }

    @PatchMapping("/update")
    public ResponseEntity<Priority> update(@RequestBody Priority priority) {
        loggingMethodName(format("PriorityController: update(%s)", priority));

        if(priority.getId() == null || priority.getId() == 0) {
            return new ResponseEntity("Missing parameter: id", NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("Missing parameter: title", NOT_ACCEPTABLE);
        }

        if (priority.getColor() == null || priority.getColor().trim().length() == 0) {
            return new ResponseEntity("Missing parameter: color", NOT_ACCEPTABLE);
        }

        priorityService.update(priority);

        return new ResponseEntity(OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Priority> delete(@RequestBody Long id) {
        loggingMethodName(format("PriorityController: delete(%d)", id));

        if(id == null || id == 0) {
            return new ResponseEntity("Missing parameter: id", NOT_ACCEPTABLE);
        }

        try {
            priorityService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity(format("Priority id=%d not found!", id), NOT_ACCEPTABLE);
        }

        return new ResponseEntity(OK);
    }

    @PostMapping("search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues) {
        loggingMethodName(format("PriorityController: search(%s)", prioritySearchValues));

        List<Priority> categories = priorityService.findByValues(prioritySearchValues.getTitle(), prioritySearchValues.getEmail());

        return ok(categories);
    }

    @PostMapping("/id")
    public ResponseEntity<Priority> findById(@RequestBody Long id) {
        loggingMethodName(format("PriorityController: findById(%d)", id));

        Priority priority;
        try {
            priority = priorityService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity(format("Priority id=%d not found!", id), NOT_ACCEPTABLE);
        }
        return ok(priority);
    }
}
