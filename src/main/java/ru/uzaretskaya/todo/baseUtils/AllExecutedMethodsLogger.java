package ru.uzaretskaya.todo.baseUtils;

import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class AllExecutedMethodsLogger {

    public static void loggingMethodName(String text) {
        System.out.println();
        System.out.println();
        log.log(Level.INFO, text);
    }
}
