package ru.uzaretskaya.todo.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.uzaretskaya.todo.business.entity.Stat;
import ru.uzaretskaya.todo.business.service.StatService;

import static java.lang.String.format;
import static org.springframework.http.ResponseEntity.ok;
import static ru.uzaretskaya.todo.business.util.AllExecutedMethodsLogger.loggingMethodName;

@RestController
public class StatController {

    private StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/stat")
    public ResponseEntity<Stat> findAll(@RequestBody String email) {
        loggingMethodName(format("StatController: findAll(%s)", email));
        return ok(statService.findStat(email));
    }

}
