package com.maxiaseo.payrll.adapters.driven.feign.exception;

public class ScheduleFileNotFoundException extends RuntimeException {
    public ScheduleFileNotFoundException(String message) {
        super(message);
    }
}
