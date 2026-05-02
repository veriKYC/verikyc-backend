package com.verikyc.repoverikycbackend.exception;

public class InvalidDocumentException extends RuntimeException{
    public InvalidDocumentException(String message){
        super(message);
    }
}
