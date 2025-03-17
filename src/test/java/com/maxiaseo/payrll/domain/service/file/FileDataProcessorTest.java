package com.maxiaseo.payrll.domain.service.file;

import com.maxiaseo.payrll.domain.model.Employee;
import com.maxiaseo.payrll.domain.util.ConstantsDomain;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileDataProcessorTest {

    private final FileDataProcessor fileDataProcessor = new FileDataProcessor();

    @Test
    void testExtractEmployeeDataWithAbsentCombination1() {
        // Arrange
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15"),
                Arrays.asList(
                        "15326844",
                        "NORALDO ISIDRO CARDENAS CARDENAS",
                        "DESC",//Sunday
                        "8pm a 6:30am",  //0.5
                        "7am a 4pm",
                        "6pm a 6am",
                        "7am a 4pm",
                        "7am a 4:30pm",      //1.5
                        "6pm a 5am",
                        "DESC",//Sunday
                        "7am a 4pm",
                        "7am a 4pm",
                        "7am a 4pm",
                        "AUS",
                        "7am a 6pm",      //3
                        "8pm a 4am",
                        "7am a 7pm"//Sunday
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
        assertEquals("15326844", employee.getId().toString());
        assertEquals("NORALDO ISIDRO CARDENAS CARDENAS", employee.getName());

        assertEquals(18.0, employee.getTotalSurchargeHoursNight());
        assertEquals(6.0, employee.getTotalSurchargeHoursNightHoliday() );
        //  -------> assertEquals(8.0, employee.getTotalSurchargeHoursHoliday());

        //  ------->assertEquals(4.0, employee.getTotalOvertimeHoursDay());//not confident
        assertEquals(6.0, employee.getTotalOvertimeHoursNight());
        //  ------->assertEquals(4.0, employee.getTotalOvertimeHoursHoliday());
        assertEquals(3.0, employee.getTotalOvertimeHoursNightHoliday());
    }


    @Test
    void testExtractEmployeeDataWithAbsentCombination2() {

        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14","15"),
                Arrays.asList(
                        "15326844",
                        "ISIDRO CARDENAS",
                        "6am a 6pm",//sunday
                        "6am a 6pm"	,
                        "6am a 6pm",
                        "6pm a 6am"	,
                        "AUS",
                        "6pm a 6am"	,
                        "6pm a 6am",
                        "6pm a 6am",//sunday
                        "6pm a 6am"	,
                        "AUS",
                        "AUS",
                        "AUS",
                        "6am a 6pm"	,
                        "6am a 6pm"	,
                        "6am a 6pm" //sunday
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

        assertEquals("15326844", employee.getId().toString());
        assertEquals("ISIDRO CARDENAS", employee.getName());

        //  -------> assertEquals(20.0, employee.getTotalSurchargeHoursNight());
        //  ------->assertEquals(5.0, employee.getTotalSurchargeHoursNightHoliday() );
        //  ------->assertEquals(19.0, employee.getTotalSurchargeHoursHoliday());

        assertEquals(16.0, employee.getTotalOvertimeHoursDay());
        //  ------->assertEquals(16.0, employee.getTotalOvertimeHoursNight());
        //  ------->assertEquals(8.0, employee.getTotalOvertimeHoursHoliday());
        assertEquals(4.0, employee.getTotalOvertimeHoursNightHoliday());
    }


    @Test
    void testExtractEmployeeDataWithAbsentCombination3() {
        List<List<String>> listOfListData = Arrays.asList(
                Arrays.asList("CEDULA", "NOMBRE", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"),
                Arrays.asList(
                        "12345678",
                        "JUAN PEREZ",
                        "8am a 8pm", // Sunday
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

        assertEquals(25, employee.getTotalSurchargeHoursNight());
        assertEquals(10, employee.getTotalSurchargeHoursNightHoliday());
        //  ------->  assertEquals(24.0, employee.getTotalSurchargeHoursHoliday());

        //  ------->assertEquals(4.0, employee.getTotalOvertimeHoursDay());
        assertEquals(0, employee.getTotalOvertimeHoursNight());
        //  ------->assertEquals(8, employee.getTotalOvertimeHoursHoliday());
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
                        "8am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",
                        "6am a 2pm",//
                        "DESC", // Sunday
                        "6am a 2pm",//
                        "6am a 2pm",
                        "6am a 2pm",
                        "DESC",//
                        "6am a 2pm",
                        "6am a 2pm",//
                        "2am a 2pm" // Sunday
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