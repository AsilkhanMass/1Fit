package com.example.onefit.exceptions;

public class SportTypeAlreadyExistsException extends RuntimeException {
    public SportTypeAlreadyExistsException(String message){
        super(message);
    }
}
