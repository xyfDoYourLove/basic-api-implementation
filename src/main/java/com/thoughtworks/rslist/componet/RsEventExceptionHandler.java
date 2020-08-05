package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity RsEventExceptionHandler(Exception e) {
        Error error = new Error();
        if (e instanceof MethodArgumentNotValidException) {
            error.setError("invalid param");
        }else {
            error.setError(e.getMessage());
        }
        return ResponseEntity.badRequest().body(error);

    }

}
