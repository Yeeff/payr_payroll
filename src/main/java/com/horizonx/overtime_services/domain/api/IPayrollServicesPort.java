package com.horizonx.overtime_services.domain.api;

import com.horizonx.overtime_services.domain.model.Employee;
import com.horizonx.overtime_services.domain.model.ScheduleEmployeesFile;

import java.io.IOException;
import java.util.List;

public interface IPayrollServicesPort {
    List<Employee> processDataByFileName(String tempFileName) throws IOException;
    List<Employee> processDataByScheduleInfo(ScheduleEmployeesFile scheduleEmployeesFile);

}
