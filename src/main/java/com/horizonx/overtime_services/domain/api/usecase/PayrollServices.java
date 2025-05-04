package com.horizonx.overtime_services.domain.api.usecase;

import com.horizonx.overtime_services.adapters.driven.feign.exception.ScheduleFileNotFoundException;
import com.horizonx.overtime_services.domain.api.IPayrollServicesPort;
import com.horizonx.overtime_services.domain.model.Employee;
import com.horizonx.overtime_services.domain.model.ScheduleEmployeesFile;
import com.horizonx.overtime_services.domain.service.file.FileDataProcessor;
import com.horizonx.overtime_services.domain.spi.IFileServicePort;
import com.horizonx.overtime_services.domain.util.ConstantsDomain;

import java.time.LocalDate;
import java.util.List;

public class PayrollServices implements IPayrollServicesPort {



    private final FileDataProcessor fileDataProcessor;
    private final IFileServicePort fileServicesClient;

    public PayrollServices(FileDataProcessor fileDataProcessor, IFileServicePort fileServicesApi) {
        this.fileDataProcessor = fileDataProcessor;
        this.fileServicesClient = fileServicesApi;
    }

    public List<Employee> processDataByFileName(String tempFileName) throws ScheduleFileNotFoundException {

        ScheduleEmployeesFile scheduleEmployeesFile= fileServicesClient.getFileWithContent(tempFileName);

        return extractSurchargesAndOvertimesFromScheduleData(scheduleEmployeesFile);

    }

    public List<Employee> processDataByScheduleInfo(ScheduleEmployeesFile scheduleEmployeesFile){

        return extractSurchargesAndOvertimesFromScheduleData(scheduleEmployeesFile);

    }


    private  List<Employee> extractSurchargesAndOvertimesFromScheduleData(ScheduleEmployeesFile scheduleEmployeesFile){
        List<List<String>> listOfListData = scheduleEmployeesFile.getContent();

        LocalDate fileSavedFortNightDate = scheduleEmployeesFile.getFortNightDate();

        return fileDataProcessor.extractEmployeeData(listOfListData,
                fileSavedFortNightDate.getYear(), fileSavedFortNightDate.getMonthValue(), fileSavedFortNightDate.getDayOfMonth(),
                ConstantsDomain.TimeFormat.valueOf(scheduleEmployeesFile.getTimeFormat()));
    }


}
