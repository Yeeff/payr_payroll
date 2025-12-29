package com.horizonx.overtime_services.domain.api.usecase;

import com.horizonx.overtime_services.adapters.driven.feign.exception.ScheduleFileNotFoundException;
import com.horizonx.overtime_services.domain.api.IPayrollServicesPort;
import com.horizonx.overtime_services.domain.model.Employee;
import com.horizonx.overtime_services.domain.model.ScheduleEmployeesFile;
import com.horizonx.overtime_services.domain.service.file.FileDataProcessor;
import com.horizonx.overtime_services.domain.service.file.FileDataValidator;
import com.horizonx.overtime_services.domain.spi.IFileServicePort;
import com.horizonx.overtime_services.domain.util.ConstantsDomain;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PayrollServices implements IPayrollServicesPort {



    private final FileDataProcessor fileDataProcessor;
    private final FileDataValidator fileDataValidator;
    private final IFileServicePort fileServicesClient;

    public PayrollServices(FileDataProcessor fileDataProcessor, IFileServicePort fileServicesApi, FileDataValidator fileDataValidator) {
        this.fileDataProcessor = fileDataProcessor;
        this.fileServicesClient = fileServicesApi;
        this.fileDataValidator = fileDataValidator;
    }

    public List<Employee> processDataByFileName(Integer formId) throws ScheduleFileNotFoundException {

        ScheduleEmployeesFile scheduleEmployeesFile= fileServicesClient.getFileWithContent(formId);

        return extractSurchargesAndOvertimesFromScheduleData(scheduleEmployeesFile);

    }

    public List<Employee> processDataByScheduleInfo(ScheduleEmployeesFile scheduleEmployeesFile){

        return extractSurchargesAndOvertimesFromScheduleData(scheduleEmployeesFile);

    }


    private  List<Employee> extractSurchargesAndOvertimesFromScheduleData(ScheduleEmployeesFile scheduleEmployeesFile){
        List<List<String>> listOfListData = scheduleEmployeesFile.getContent();

        LocalDate fileSavedFortNightDate = scheduleEmployeesFile.getFortNightDate();

        Map<String, String> errorsMap = fileDataValidator.getErrorsFormat(
                fileSavedFortNightDate.getYear(), fileSavedFortNightDate.getMonthValue(), fileSavedFortNightDate.getDayOfMonth(),
                listOfListData,
                ConstantsDomain.TimeFormat.valueOf(scheduleEmployeesFile.getTimeFormat())
        );

        if( ! errorsMap.isEmpty()) {
            //throw new IncorrectFormatExcelValuesException("", errorsMap );
        }

        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData,
                fileSavedFortNightDate.getYear(), fileSavedFortNightDate.getMonthValue(), fileSavedFortNightDate.getDayOfMonth(),
                ConstantsDomain.TimeFormat.valueOf(scheduleEmployeesFile.getTimeFormat()));

        return result;
    }


}
