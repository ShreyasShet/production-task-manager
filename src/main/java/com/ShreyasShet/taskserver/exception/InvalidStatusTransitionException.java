package com.ShreyasShet.taskserver.exception;

public class InvalidStatusTransitionException extends RuntimeException {
    
    public InvalidStatusTransitionException(String from, String to) {
        super("Cannot change status from '" + from + "' to '" + to + "'");
    }
}