package com.maxiaseo.payrll.domain.api;

import com.maxiaseo.payrll.domain.model.Employee;

import java.io.IOException;
import java.util.List;

public interface IPayrollServicesPort {
    List<Employee> processDataByFileName(String tempFileName) throws IOException;

}
