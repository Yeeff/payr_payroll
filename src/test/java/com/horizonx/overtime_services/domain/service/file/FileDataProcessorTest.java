package com.horizonx.overtime_services.domain.service.file;

import com.horizonx.overtime_services.adapters.driven.jpa.mysql.adapter.PayrollPersistentAdapter;
import com.horizonx.overtime_services.domain.model.Employee;
import com.horizonx.overtime_services.domain.util.ConstantsDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileDataProcessorTest {

    @Mock
    private  PayrollPersistentAdapter payrollPersistentAdapter;

    @InjectMocks
    private  FileDataProcessor fileDataProcessor;

    static final Integer NO_VALUES_FOUND = -1;

    @Test
    void testExtractEmployeeDataWithAbsent() {

        // Arrange
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15"),
                Arrays.asList(
                        "15326844",
                        "NORALDO ISIDRO CARDENAS CARDENAS",
                        "DESC",//1 Sunday
                        "8pm a 6:30am",
                        "7am a 4pm",
                        "6pm a 6am",
                        "7am a 4pm",
                        "7am a 4:30pm",
                        "6pm a 5am",
                        "DESC",// 8 Sunday
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "AUS",
                        "7am a 6pm",
                        "8pm a 4am",
                        "7am a 7pm"// 15 Sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        Mockito.when(payrollPersistentAdapter.getLastHoursWorkedInTheLastWeekByFortnight(Mockito.any()))
                .thenReturn(NO_VALUES_FOUND);

        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay, ConstantsDomain.TimeFormat.REGULAR);

        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);
        assertEquals("15326844", employee.getId().toString());
        assertEquals("NORALDO ISIDRO CARDENAS CARDENAS", employee.getName());

        assertEquals(18.0, employee.getTotalSurchargeHoursNight());
        assertEquals(3.0, employee.getTotalSurchargeHoursNightHoliday() );
        assertEquals(8.0, employee.getTotalSurchargeHoursHoliday());

        assertEquals(2.5, employee.getTotalOvertimeHoursDay());
        assertEquals(5.5, employee.getTotalOvertimeHoursNight());
        assertEquals(4.0, employee.getTotalOvertimeHoursHoliday());
        assertEquals(4.0, employee.getTotalOvertimeHoursNightHoliday());

        assertEquals(0.0, employee.getTotalOvertimeSurchargeHoursHoliday() );
        assertEquals(0.0, employee.getTotalOvertimeSurchargeHoursNightHoliday() );
    }


    @Test
    void testExtractEmployeeDataWithAbsentAndNoDataInPreviousFortnight() {

        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15"),
                Arrays.asList(
                        "15326844",
                        "ISIDRO CARDENAS",
                        "6am a 6pm",// 1 sunday
                        "6am a 6pm"	,
                        "6am a 6pm" ,
                        "6pm a 6am"	,
                        "AUS",
                        "6pm a 6am"	,
                        "6pm a 6am" ,
                        "6pm a 6am",// 8 sunday
                        "6pm a 6am"	,
                        "AUS",
                        "AUS",
                        "AUS",
                        "6am a 6pm"	,
                        "6am a 6pm"	,
                        "6am a 6pm" // 15 sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        Mockito.when(payrollPersistentAdapter.getLastHoursWorkedInTheLastWeekByFortnight(Mockito.any()))
                .thenReturn(NO_VALUES_FOUND);

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay, ConstantsDomain.TimeFormat.REGULAR);

        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);

        assertEquals("15326844", employee.getId().toString());
        assertEquals("ISIDRO CARDENAS", employee.getName());

        assertEquals(20.0, employee.getTotalSurchargeHoursNight());
        assertEquals(5.0, employee.getTotalSurchargeHoursNightHoliday() );
        assertEquals(11.0, employee.getTotalSurchargeHoursHoliday());

        assertEquals(16.0, employee.getTotalOvertimeHoursDay());
        assertEquals(16.0, employee.getTotalOvertimeHoursNight());
        assertEquals(4.0, employee.getTotalOvertimeHoursHoliday());
        assertEquals(4.0, employee.getTotalOvertimeHoursNightHoliday());

        assertEquals(12, employee.getTotalOvertimeSurchargeHoursHoliday());
        assertEquals(0, employee.getTotalOvertimeSurchargeHoursNightHoliday());
    }

    @Test
    void testExtractEmployeeDataWithNoDataInPreviousFortnight() {
        //January first 2025!
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15"),
                Arrays.asList(
                        "15326844",
                        "ISIDRO CARDENAS",
                        "7am a 4pm"	,
                        "7am a 4pm"	,
                        "7am a 4pm"	,
                        "7am a 2pm"	,
                        "7am a 4pm", //sunday
                        "7am a 4pm"	,
                        "7am a 4pm"	,
                        "7am a 4pm"	,
                        "7am a 4pm"	,
                        "7am a 4pm"	,
                        "7am a 2pm"	,
                        "7am a 4pm",//sunday
                        "7am a 4pm"	,
                        "7am a 4pm"	,
                        "7am a 4pm"
                )
        );
        int year = 2025;
        int month = 1;
        int initDay = 1;

        Mockito.when(payrollPersistentAdapter.getLastHoursWorkedInTheLastWeekByFortnight(Mockito.any()))
                .thenReturn(NO_VALUES_FOUND);

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay, ConstantsDomain.TimeFormat.REGULAR);

        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);

        assertEquals("15326844", employee.getId().toString());
        assertEquals("ISIDRO CARDENAS", employee.getName());

        assertEquals(0.0, employee.getTotalSurchargeHoursNight());
        assertEquals(0.0, employee.getTotalSurchargeHoursNightHoliday() );
        assertEquals(0.0, employee.getTotalSurchargeHoursHoliday());

        assertEquals(0.0, employee.getTotalOvertimeHoursDay());
        assertEquals(0.0, employee.getTotalOvertimeHoursNight());
        assertEquals(0.0, employee.getTotalOvertimeHoursHoliday());
        assertEquals(0.0, employee.getTotalOvertimeHoursNightHoliday());

        assertEquals(16, employee.getTotalOvertimeSurchargeHoursHoliday());
        assertEquals(0, employee.getTotalOvertimeSurchargeHoursNightHoliday());
    }

    @Test
    void testExtractEmployeeDataWithAbsentCombination3() {
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"),
                Arrays.asList(
                        "12345678",
                        "JUAN PEREZ",
                        "8am a 8pm", // Sunday  22
                        "8am a 5pm",
                        "AUS",
                        "9pm a 5am",//
                        "AUS",
                        "8am a 5pm",
                        "8pm a 4am",//
                        "8am a 8pm", // Sunday
                        "9pm a 5am",//
                        "AUS",
                        "6am a 10am",
                        "9pm a 1am",//
                        "9am a 9pm",
                        "10pm a 6am",//
                        "8am a 5pm" // Sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay, ConstantsDomain.TimeFormat.REGULAR);

        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);

        assertEquals("12345678", employee.getId().toString());
        assertEquals("JUAN PEREZ", employee.getName());

        assertEquals(22, employee.getTotalSurchargeHoursNight());
        assertEquals(8, employee.getTotalSurchargeHoursNightHoliday());
        assertEquals(24.0, employee.getTotalSurchargeHoursHoliday());

        assertEquals(4.0, employee.getTotalOvertimeHoursDay());
        assertEquals(0, employee.getTotalOvertimeHoursNight());
        assertEquals(8, employee.getTotalOvertimeHoursHoliday());
        assertEquals(0, employee.getTotalOvertimeHoursNightHoliday());
    }

    @Test
    void testExtractEmployeeDataWithNoSurchargesAndNoOvertimes() {
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"),
                Arrays.asList(
                        "12345678",
                        "JUAN PEREZ",
                        "DESC", // Sunday
                        "6am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "DESC", // Sunday
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",//
                        "DESC" // Sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay, ConstantsDomain.TimeFormat.REGULAR);

        assertNotNull(result);
        assertEquals(0, result.size());

        assertEquals(true, result.isEmpty() );
    }

    @Test
    void testExtractEmployeeDataWithCompensatoryDays() {
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"),
                Arrays.asList(
                        "12345678",
                        "JUAN PEREZ",
                        "DESC", // Sunday
                        "8am a 3pm",
                        "6am a 3pm",
                        "6am a 3pm",//
                        "6am a 3pm",
                        "6am a 3pm",
                        "6am a 3pm",//
                        "DESC", // Sunday
                        "6am a 3pm",//
                        "6am a 3pm",
                        "6am a 3pm",
                        "DESC",//
                        "6am a 3pm",
                        "6am a 1pm",//
                        "2am a 3pm" // Sunday
                )
        );
        int year = 2024;
        int month = 9;
        int initDay = 1;

        // Act
        List<Employee> result = fileDataProcessor.extractEmployeeData(listOfListData, year, month, initDay, ConstantsDomain.TimeFormat.REGULAR);

        assertNotNull(result);
        assertEquals(1, result.size());
        Employee employee = result.get(0);

        assertEquals("12345678", employee.getId().toString());
        assertEquals("JUAN PEREZ", employee.getName());

        assertEquals(0, employee.getTotalSurchargeHoursNight());
        assertEquals(0, employee.getTotalSurchargeHoursNightHoliday());
        assertEquals(0, employee.getTotalSurchargeHoursHoliday());

        assertEquals(0, employee.getTotalOvertimeHoursDay());
        assertEquals(0, employee.getTotalOvertimeHoursNight());
        assertEquals(0, employee.getTotalOvertimeHoursHoliday());
        assertEquals(0, employee.getTotalOvertimeHoursNightHoliday());

        assertEquals(8, employee.getTotalOvertimeSurchargeHoursHoliday());
        assertEquals(4, employee.getTotalOvertimeSurchargeHoursNightHoliday());

    }


}