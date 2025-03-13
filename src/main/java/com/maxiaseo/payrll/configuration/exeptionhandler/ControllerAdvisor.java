package com.maxiaseo.payrll.configuration.exeptionhandler;

import com.maxiaseo.payrll.adapters.driven.feign.exception.ScheduleFileNotFoundException;
import com.maxiaseo.payrll.adapters.driven.feign.exception.FileServiceException;
import com.maxiaseo.payrll.domain.util.ConstantsDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvisor {

    @ExceptionHandler(ScheduleFileNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleScheduleFileNotFoundException(ScheduleFileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(String.format(ConstantsDomain.FILE_NOT_FOUND_MESSAGE_ERROR, ex.getMessage()) ,
                        HttpStatus.NOT_FOUND.toString(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(FileServiceException.class)
    public ResponseEntity<ExceptionResponse> handleFileServiceException(FileServiceException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(String.format(ConstantsDomain.FILE_SERVICE_MESSAGE_ERROR, ex.getMessage()) ,
                        HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        LocalDateTime.now()));
    }

}
