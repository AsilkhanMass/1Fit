package com.example.onefit.exceptions;

public class SubscriptionNotFoundException extends RuntimeException{
    public SubscriptionNotFoundException(String message){
        super(message);
    }
}
