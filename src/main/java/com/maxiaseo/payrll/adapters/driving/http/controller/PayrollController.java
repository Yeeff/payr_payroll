package com.maxiaseo.payrll.adapters.driving.http.controller;

import com.maxiaseo.payrll.adapters.driving.http.dto.EmployeeResponseDto;
import com.maxiaseo.payrll.adapters.driving.http.mapper.IEmployeeMapper;
import com.maxiaseo.payrll.domain.api.IPayrollServicesPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor

@RequestMapping("/api/payroll")
public class PayrollController {

    private final IPayrollServicesPort payrollServices;
    private final IEmployeeMapper employeeMapper;

    private File tempFile; // Store reference to the temp file


    @GetMapping("/process-info/{fileName}")
    public ResponseEntity<List<EmployeeResponseDto>> handleFileUpload(
            @PathVariable String fileName) throws IOException {

        List<EmployeeResponseDto>  result = employeeMapper.toEmployeeResponseDtoList(
                payrollServices.processDataByFileName(fileName)
        );

        return ResponseEntity.ok(result);
    }

}
