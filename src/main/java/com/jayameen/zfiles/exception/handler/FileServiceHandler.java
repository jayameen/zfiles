package com.jayameen.zfiles.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FileServiceHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exception(Exception exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }

}
