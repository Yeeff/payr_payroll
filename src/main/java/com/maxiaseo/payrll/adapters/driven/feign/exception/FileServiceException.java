package com.maxiaseo.payrll.adapters.driven.feign.exception;

public class FileServiceException extends RuntimeException {
    public FileServiceException(String message) {
        super(message);
    }
}
