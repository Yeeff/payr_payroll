package com.maxiaseo.payrll.domain.api.usecase;

import com.maxiaseo.payrll.adapters.driven.feign.exception.ScheduleFileNotFoundException;
import com.maxiaseo.payrll.domain.api.IPayrollServicesPort;
import com.maxiaseo.payrll.domain.model.Employee;
import com.maxiaseo.payrll.domain.model.ScheduleEmployeesFile;
import com.maxiaseo.payrll.domain.service.file.FileDataProcessor;
import com.maxiaseo.payrll.domain.spi.IFileServicePort;
import com.maxiaseo.payrll.domain.util.ConstantsDomain;

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

        List<List<String>> listOfListData = scheduleEmployeesFile.getContent();

        LocalDate fileSavedFortNightDate = scheduleEmployeesFile.getFortNightDate();

        return fileDataProcessor.extractEmployeeData(listOfListData,
                fileSavedFortNightDate.getYear(), fileSavedFortNightDate.getMonthValue(), fileSavedFortNightDate.getDayOfMonth(),
                ConstantsDomain.TimeFormat.valueOf(scheduleEmployeesFile.getTimeFormat()));

        /*dataInMemory = excelManagerAdapter.updateEmployeeDataInExcel(dataInMemory, employees );

        FileAdministrator.overwriteTempFile(tempFileName, dataInMemory);*/



    }


}
