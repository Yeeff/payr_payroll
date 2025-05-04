package com.horizonx.overtime_services.adapters.driven.feign.exception;

public class ScheduleFileNotFoundException extends RuntimeException {
    public ScheduleFileNotFoundException(String message) {
        super(message);
    }
}
