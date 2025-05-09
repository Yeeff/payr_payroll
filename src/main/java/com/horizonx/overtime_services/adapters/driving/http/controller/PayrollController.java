package com.horizonx.overtime_services.adapters.driving.http.controller;

import com.horizonx.overtime_services.adapters.driving.http.dto.EmployeeResponseDto;
import com.horizonx.overtime_services.adapters.driving.http.mapper.IEmployeeMapper;
import com.horizonx.overtime_services.domain.api.IPayrollServicesPort;
import com.horizonx.overtime_services.domain.model.Employee;
import com.horizonx.overtime_services.domain.model.ScheduleEmployeesFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor

@RequestMapping("/api/payroll")
public class PayrollController {

    private final IPayrollServicesPort payrollServices;
    private final IEmployeeMapper employeeMapper;

    private File tempFile; // Store reference to the temp file


    @GetMapping("/process-info/{formId}")
    public ResponseEntity<List<EmployeeResponseDto>> handleFileUpload(
            @PathVariable Integer formId) throws IOException {

        List<EmployeeResponseDto>  result = employeeMapper.toEmployeeResponseDtoList(
                payrollServices.processDataByFileName(formId)
        );

        return ResponseEntity.ok(result);
    }

    @PostMapping("/process")
    public List<Employee> processPayroll(@RequestBody ScheduleEmployeesFile data) {
        return payrollServices.processDataByScheduleInfo(data);
    }


}
