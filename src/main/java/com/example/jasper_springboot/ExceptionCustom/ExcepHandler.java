package com.example.jasper_springboot.ExceptionCustom;

import com.example.jasper_springboot.Response.Message;
import org.hibernate.annotations.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestControllerAdvice
public class ExcepHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Message> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Có lỗi xảy ra"));
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Message> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message("Kiểm tra lại kiểu dữ liệu"));
    }
}
