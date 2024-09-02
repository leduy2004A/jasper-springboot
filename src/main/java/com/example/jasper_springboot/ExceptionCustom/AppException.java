package com.example.jasper_springboot.ExceptionCustom;

import com.example.jasper_springboot.Response.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppException extends RuntimeException{
    private int code;
    private String message;
}
