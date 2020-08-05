package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class RsEventExceptionHandler {

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity RsEventExceptionHandler(Exception e) {
        Error error = new Error();
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            if (methodArgumentNotValidException.getBindingResult().getTarget() instanceof RsEvent) {
                error.setError("invalid param");
                log.error("invalid param");
            }
            if (methodArgumentNotValidException.getBindingResult().getTarget() instanceof User) {
                error.setError("invalid user");
                log.error("invalid user");
            }
        }else {
            error.setError(e.getMessage());
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().body(error);

    }

}
