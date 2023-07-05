package com.LoginAndForget.LFF.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String s) {
        super(s);
    }
}
