package com.jayameen.zfiles.exception.handler;

import com.jayameen.zfiles.dto.AppResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FileServiceHandler {

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<?> nullPointerException(NullPointerException exception) {
        AppResponse appResponse = new AppResponse();
        appResponse.setStatus("error");
        appResponse.setDescription(exception.getCause().toString());
        return ResponseEntity.internalServerError().body(appResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> exception(Exception exception) {
        AppResponse appResponse = new AppResponse();
        appResponse.setStatus("error");
        appResponse.setDescription(exception.getMessage());
        return ResponseEntity.internalServerError().body(appResponse);
    }

}
