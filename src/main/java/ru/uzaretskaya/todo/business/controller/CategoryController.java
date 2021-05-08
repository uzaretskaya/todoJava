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
import ru.uzaretskaya.todo.business.entity.Category;
import ru.uzaretskaya.todo.business.search.CategorySearchValues;
import ru.uzaretskaya.todo.business.service.CategoryService;
import ru.uzaretskaya.todo.business.util.AllExecutedMethodsLogger;

import java.util.List;
import java.util.NoSuchElementException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<Category>> findAll(@RequestBody String email) {
        AllExecutedMethodsLogger.loggingMethodName(format("CategoryController: findAll(%s)", email));
        return ResponseEntity.ok(categoryService.findAll(email));
    }

    @PutMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        AllExecutedMethodsLogger.loggingMethodName(format("CategoryController: add(%s)", category));

        if(category.getId() != null && category.getId() != 0) {
            return new ResponseEntity("Redundant parameter: ID must be NULL", NOT_ACCEPTABLE);
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("Missing parameter: title", NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(categoryService.add(category));
    }

    @PatchMapping("/update")
    public ResponseEntity<Category> update(@RequestBody Category category) {
        AllExecutedMethodsLogger.loggingMethodName(format("CategoryController: update(%s)", category));

        if(category.getId() == null || category.getId() == 0) {
            return new ResponseEntity("Missing parameter: id", NOT_ACCEPTABLE);
        }

        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("Missing parameter: title", NOT_ACCEPTABLE);
        }

        categoryService.update(category);

        return new ResponseEntity(OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Category> delete(@RequestBody Long id) {
        AllExecutedMethodsLogger.loggingMethodName(format("CategoryController: delete(%d)", id));

        if(id == null || id == 0) {
            return new ResponseEntity("Missing parameter: id", NOT_ACCEPTABLE);
        }

        try {
            categoryService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity(format("Category id=%d not found!", id), NOT_ACCEPTABLE);
        }

        return new ResponseEntity(OK);
    }

    @PostMapping("search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) {
        AllExecutedMethodsLogger.loggingMethodName(format("CategoryController: search(%s)", categorySearchValues));

        List<Category> categories = categoryService.findByValues(categorySearchValues.getTitle(), categorySearchValues.getEmail());

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/id")
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        AllExecutedMethodsLogger.loggingMethodName(format("CategoryController: findById(%d)", id));

        Category category;
        try {
            category = categoryService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity(format("Category id=%d not found!", id), NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(category);
    }
}
