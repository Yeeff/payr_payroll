package com.horizonx.overtime_services.adapters.driven.feign.exception;

public class FileServiceException extends RuntimeException {
    public FileServiceException(String message) {
        super(message);
    }
}
