package com.ShreyasShet.taskserver.exception;

public class ConflictException extends RuntimeException {
    
    public ConflictException(String msg) {
        super(msg);
    }
}