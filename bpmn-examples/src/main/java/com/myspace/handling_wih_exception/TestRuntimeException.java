package com.myspace.handling_wih_exception;

public class TestRuntimeException extends RuntimeException {

    private String message;

    public TestRuntimeException(String message){
        super();
        this.message = message;
    }
    
}
