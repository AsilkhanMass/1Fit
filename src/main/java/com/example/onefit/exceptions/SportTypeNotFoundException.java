package com.example.onefit.exceptions;

public class SportTypeNotFoundException extends RuntimeException{
    public SportTypeNotFoundException(String message){
        super(message);
    }
}
