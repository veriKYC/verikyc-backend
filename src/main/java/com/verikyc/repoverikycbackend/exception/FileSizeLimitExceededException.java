package com.verikyc.repoverikycbackend.exception;

public class FileSizeLimitExceededException extends RuntimeException{
    public FileSizeLimitExceededException(String message) {
        super(message);
    }
}
