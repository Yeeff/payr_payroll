package com.maxiaseo.payrll.domain.service.file;

import com.maxiaseo.payrll.domain.model.*;
import com.maxiaseo.payrll.domain.service.Maxiaseo;
import com.maxiaseo.payrll.domain.service.processor.OvertimeCalculator;
import com.maxiaseo.payrll.domain.service.processor.OvertimeSurchargeCalculator;
import com.maxiaseo.payrll.domain.service.processor.SurchargeCalculator;
import com.maxiaseo.payrll.domain.spi.IPayrollPersistentPort;
import com.maxiaseo.payrll.domain.util.TimeRange;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.maxiaseo.payrll.domain.util.ConstantsDomain.*;

public class FileDataProcessor {


    private Employee employee;
    Map<String, String> errorsMap ;
    private final Byte A_NUMBER_OF_PAST_WORK_DAYS_THAT_GUARANTEE_EXCEED_THE_MAX_HOURS_PER_WEEK = 50;
    private final Integer NO_VALUES_FOUND = -1;

    Integer hoursWorkedPerWeek ;

    Byte quantityOfWeeksStarted = 0;
    Boolean isQuantityOfPastWorkedDaysAdded;

    IPayrollPersistentPort payrollPersistent;

    public FileDataProcessor(IPayrollPersistentPort payrollPersistent) {
        this.payrollPersistent = payrollPersistent;
        this.hoursWorkedPerWeek = 0;
        errorsMap = new HashMap<>();
        quantityOfWeeksStarted = 0;
        isQuantityOfPastWorkedDaysAdded = false;
    }


    public  List<Employee> extractEmployeeData(List<List<String>> listOfListData, Integer year, Integer month, Integer initDay, TimeFormat timeFormatType){

        LocalDate initDateOfFortnight = LocalDate.of(year,month, initDay);
        LocalDate currentDate;

        List<Employee> employees = new ArrayList<>();

        for (int i = FIRST_ROW_WITH_VALID_DATA_INDEX; i <= listOfListData.size()-1; i++) {

            employee = new Employee();

            currentDate = initDateOfFortnight;

            List<String> dataRowList = listOfListData.get(i);

            employee.setId(extractDocumentId(dataRowList));
            employee.setName(extractName(dataRowList));

            for (int j = FIRST_COLUM_WITH_VALID_DATA_INDEX; j < dataRowList.size(); j++) {

                if(currentDate.getDayOfWeek() == DayOfWeek.MONDAY)
                    quantityOfWeeksStarted++;

                if(quantityOfWeeksStarted == 0 && !isQuantityOfPastWorkedDaysAdded) addPastWorkedDays(initDateOfFortnight, currentDate.getDayOfWeek());

                if (dataRowList.get(j) != null) {
                    String cellValue = dataRowList.get(j);

                    if (isValidTimeRange(cellValue, timeFormatType) ){
                        TimeRange currentTimeRange = getInitTimeAndEndTime(cellValue, currentDate, timeFormatType);
                        addSurchargeOvertimesToEmployeeBasedOnTimeRange( currentTimeRange.getStartTime(), currentTimeRange.getEndTime());

                        addHoursWorkedBasedOnTimeRange(currentTimeRange);
                    } else if(isValidAbsenceReason(cellValue) && !cellValue.equals(AbsenceReasonsEnum.AUS.toString())  ) {
                        addHoursWorkedBasedOnAbsentReason(MAX_HOURS_BY_DAY);
                        addAbsenteeismReasonToEmployee(cellValue);
                    }
                }

                if(currentDate.getDayOfWeek() == DayOfWeek.SUNDAY)
                    hoursWorkedPerWeek = 0;

                currentDate =currentDate.plusDays(1);

            }
            employees = addCurrentEmployeeToList(employees);
            quantityOfWeeksStarted = 0;
            isQuantityOfPastWorkedDaysAdded = false;

        }
        return employees;
    }


    private void addSurchargeOvertimesToEmployeeBasedOnTimeRange(LocalDateTime startTime, LocalDateTime endTime) {

        if (hoursWorkedPerWeek >= MAX_HOURS_BY_WEEK && startTime.getDayOfWeek() == DayOfWeek.SUNDAY) {
            addOvertimeSurchargeToEmployee(startTime, endTime);
        } else {
            for (Surcharge surcharge : SurchargeCalculator.getSurchargeList(startTime, endTime)) {
                if (surcharge.getQuantityOfMinutes() != 0) {
                    employee.addNewSurcharge(surcharge);
                }
            }

            for (Overtime overtime : OvertimeCalculator.getOvertimeList(startTime, endTime)) {
                if (overtime.getQuantityOfMinutes() != 0) {
                    employee.addNewOverTime(overtime);
                }
            }
        }

    }

    public static LocalDateTime parseTime(String timeString, LocalDate date, TimeFormat formatType) {
        DateTimeFormatter formatter;

        formatter = switch (formatType){
            case REGULAR -> new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("[h:mma][ha]")
                    .toFormatter(Locale.ENGLISH);

            case MILITARY ->  new DateTimeFormatterBuilder()
                    .appendPattern("[H:mm][H]") // Handle both "14:00" and "14"
                    .toFormatter();

            case MILITARY_WITHOUT_COLON -> new DateTimeFormatterBuilder()
                    .appendPattern("[HHmm]")
                    .toFormatter();
        };

        try {
            // Parse the time string into LocalTime
            LocalTime time = LocalTime.parse(timeString, formatter);

            // Combine the parsed time with the provided date to create a LocalDateTime
            return LocalDateTime.of(date, time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format: " + timeString);
        }
    }

    private boolean isValidTimeRange(String timeRange, TimeFormat formatType) {
        String timePattern;

        timePattern = switch (formatType){
            case REGULAR -> "^([1-9]|1[0-2])(:[0-5][0-9])?([ap]m) a ([1-9]|1[0-2])(:[0-5][0-9])?([ap]m)$";
            case MILITARY -> "^([01]?[0-9]|2[0-3])(:[0-5][0-9])? a ([01]?[0-9]|2[0-3])(:[0-5][0-9])?$";
            case MILITARY_WITHOUT_COLON -> "^(?:[01][0-9]|2[0-3])[0-5][0-9] A (?:[01][0-9]|2[0-3])[0-5][0-9]$";
        };

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher = pattern.matcher(timeRange);

        // Return true if it matches, otherwise false
        return matcher.matches();
    }

    private TimeRange getInitTimeAndEndTime( String schedule, LocalDate date, TimeFormat timeFormat){

        Maxiaseo maxi = new Maxiaseo();

        String[] times = timeFormat == TimeFormat.MILITARY_WITHOUT_COLON
                ? schedule.split(" A")
                : schedule.split(" a");

        LocalDateTime startTime = parseTime(times[0].trim(), date, timeFormat);
        LocalDateTime endTime = parseTime(times[1].trim(), date, timeFormat);

        if(endTime.isBefore(startTime))
            endTime = endTime.plusDays(1);

        TimeRange timeRange = new TimeRange(startTime, endTime);

        return maxi.validateLunchHour(timeRange);
    }

    private void addPastWorkedDays(LocalDate initDateFortNight, DayOfWeek dayOfWeek){

        Integer hoursWorkedInTheLastFortnight = payrollPersistent.getLastHoursWorkedInTheLastWeekByFortnight(initDateFortNight);

        if(hoursWorkedInTheLastFortnight != NO_VALUES_FOUND){
            hoursWorkedPerWeek += hoursWorkedInTheLastFortnight;
        }else{
            hoursWorkedPerWeek +=  switch (dayOfWeek){
                case TUESDAY -> 8; case WEDNESDAY -> 16; case THURSDAY -> 24; case FRIDAY -> 32; case SATURDAY -> 40; case SUNDAY -> MAX_HOURS_BY_WEEK; default -> MAX_HOURS_BY_WEEK;
            };
        }

        isQuantityOfPastWorkedDaysAdded = true;
    }

    private void addHoursWorkedBasedOnTimeRange(TimeRange currentTimeRange){

        Integer hoursWorkedPerDay = (int) Duration.between(
                currentTimeRange.getStartTime(), currentTimeRange.getEndTime()
        ).toHours();

        if (hoursWorkedPerDay > MAX_HOURS_BY_DAY)
            hoursWorkedPerWeek += MAX_HOURS_BY_DAY;
        else
            hoursWorkedPerWeek += hoursWorkedPerDay;



    }

    private void addHoursWorkedBasedOnAbsentReason( Integer hourToSum){

            hoursWorkedPerWeek += hourToSum ;
    }

    private String extractName(List<String> dataRowList){
                 return dataRowList.get(EMPLOYEE_NAME_INDEX);
    }

    private Long extractDocumentId(List<String> dataRowList){
        return Long.valueOf(dataRowList.get(EMPLOYEE_DOCUMENT_ID_INDEX));
    }

    private List<Employee> addCurrentEmployeeToList(List<Employee> employees){
        if(
                !employee.getSurcharges().isEmpty() ||
                        !employee.getOvertimes().isEmpty() ||
                        !employee.getOvertimeSurcharges().isEmpty()
        ) employees.add(employee);
        return employees;
    }

    private void addOvertimeSurchargeToEmployee (LocalDateTime startTime, LocalDateTime endTime){
        for (OvertimeSurcharge overtimeSurcharge : OvertimeSurchargeCalculator.getOvertimeSurchargeList(startTime, endTime)) {
            if (overtimeSurcharge.getQuantityOfMinutes() != 0) {
                employee.addNewOverTimeSurcharge(overtimeSurcharge);
            }
        }
    }

    private void addAbsenteeismReasonToEmployee(String reason){
        AbsenteeismReason absenteeismReason = new AbsenteeismReason();
        absenteeismReason.setAbsenceReasonsEnum(AbsenceReasonsEnum.valueOf(reason));
        absenteeismReason.setQuantityOfHours(MAX_HOURS_BY_DAY);
        employee.addNewAbsenteeismReason(absenteeismReason);
    }


    private boolean isValidAbsenceReason(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            AbsenceReasonsEnum.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }



}
