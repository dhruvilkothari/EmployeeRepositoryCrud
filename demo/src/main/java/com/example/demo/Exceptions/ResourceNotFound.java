package com.example.demo.Exceptions;


import org.springframework.web.bind.annotation.ExceptionHandler;
public class ResourceNotFound extends Exception {
    public ResourceNotFound(String message){
        super(message);
    }
}
