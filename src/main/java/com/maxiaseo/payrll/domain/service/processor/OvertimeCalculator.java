package com.maxiaseo.payrll.domain.service.processor;

import com.maxiaseo.payrll.domain.model.Money;
import com.maxiaseo.payrll.domain.model.Overtime;
import com.maxiaseo.payrll.domain.util.ConstantsDomain;
import com.maxiaseo.payrll.domain.util.ConstantsDomain.OvertimeTypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.maxiaseo.payrll.domain.util.ConstantsDomain.*;

public class OvertimeCalculator {

    private static final Money DAY_RATE_MULTIPLIER = new Money("1.25");
    private static final Money NIGHT_RATE_MULTIPLIER = new Money("1.75");
    private static final Money HOLIDAY_RATE_MULTIPLIER = new Money("2.00");
    private static final Money NIGHT_HOLIDAY_RATE_MULTIPLIER = new Money("2.50");

    private OvertimeCalculator() {
    }


    public static List<Overtime> getOvertimeList(LocalDateTime start, LocalDateTime end, Integer maximumLegalHours, String monthlyPayment) {

        List<Overtime> overtimeList = new ArrayList<>();

        Overtime overtimeDay = new Overtime();
        Overtime overtimeNight =  new Overtime();
        Overtime overtimeHoliday =  new Overtime();
        Overtime overtimeHolidayNight =  new Overtime();

        LocalDateTime currentTime = start.plusMinutes(STEP_IN_MINUTES + (maximumLegalHours * 60) );

        while (currentTime.isBefore(end) || currentTime.isEqual(end)) {

            if (getOvertimeType(currentTime) == OvertimeTypeEnum.DAY) {
                overtimeDay = increaseValueOfStepToOvertime(overtimeDay, currentTime, OvertimeTypeEnum.DAY);
            }
            if (getOvertimeType(currentTime) == OvertimeTypeEnum.NIGHT) {
                overtimeNight = increaseValueOfStepToOvertime(overtimeNight, currentTime, OvertimeTypeEnum.NIGHT);
            }
            if (getOvertimeType(currentTime) == OvertimeTypeEnum.HOLIDAY) {
                overtimeHoliday = increaseValueOfStepToOvertime(overtimeHoliday, currentTime, OvertimeTypeEnum.HOLIDAY);
            }
            if (getOvertimeType(currentTime) == OvertimeTypeEnum.NIGHT_HOLIDAY) {
                overtimeHolidayNight = increaseValueOfStepToOvertime(overtimeHolidayNight, currentTime, OvertimeTypeEnum.NIGHT_HOLIDAY);
            }

            currentTime = currentTime.plusMinutes(STEP_IN_MINUTES);
        }

        if (overtimeDay.getQuantityOfMinutes() != 0) overtimeList.add(overtimeDay);
        if (overtimeNight.getQuantityOfMinutes() != 0) overtimeList.add(overtimeNight);
        if (overtimeHoliday.getQuantityOfMinutes() != 0) overtimeList.add(overtimeHoliday);
        if (overtimeHolidayNight.getQuantityOfMinutes() != 0) overtimeList.add(overtimeHolidayNight);

        calculateOvertimeCosts(overtimeList, monthlyPayment);

        return overtimeList;
    }

    private static OvertimeTypeEnum getOvertimeType(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();

        if (time.equals(LocalTime.of(0, 0))) {
            dateTime = dateTime.minusMinutes(1);
        }

        DayOfWeek day = dateTime.getDayOfWeek();
        boolean isNightSurcharge = (time.isAfter(NIGHT_START) || time.isBefore(NIGHT_END) || time.equals(NIGHT_END));
        boolean isSunday = day == DayOfWeek.SUNDAY;

        if (isSunday && isNightSurcharge) {
            return OvertimeTypeEnum.NIGHT_HOLIDAY;
        } else if (isSunday) {
            return OvertimeTypeEnum.HOLIDAY;
        } else if (isNightSurcharge) {
            return OvertimeTypeEnum.NIGHT;
        } else {    
            return OvertimeTypeEnum.DAY;
        }
    }


    private static Overtime increaseValueOfStepToOvertime(Overtime overtime, LocalDateTime cur, OvertimeTypeEnum type ){
        overtime.setOvertimeTypeEnum(type);

        if (overtime.getStart() == null)   {
            overtime.setStart(cur.minusMinutes(STEP_IN_MINUTES));
        }

        overtime.setEnd(cur);
        overtime.increasValueOfStep();

        return overtime;
    }

    public static void calculateOvertimeCosts(List<Overtime> overtimeList, String monthlyPayment) {

        Money baseMonthlyPayment = new Money(monthlyPayment);
        Money legalHoursWorkedPerMonth = new Money(String.valueOf(MAXIMUM_HOURS_PER_WEEK * 5));
        Money baseHourlyRate = baseMonthlyPayment.divide(legalHoursWorkedPerMonth);

        for (Overtime overtime : overtimeList) {
            Money overtimeRate = getOvertimeMultiplier(overtime.getOvertimeTypeEnum());
            Money minutesWorked = new Money(overtime.getQuantityOfMinutes().toString());
            Money hourlyRate = baseHourlyRate.multiply(overtimeRate);

            Money overtimeCost = hourlyRate.multiply(minutesWorked).divide(new Money(String.valueOf(60)));

            overtime.setMoneyCost(overtimeCost);
        }
    }
    private static Money getOvertimeMultiplier(ConstantsDomain.OvertimeTypeEnum overtimeType) {
        return switch (overtimeType) {
            case DAY -> DAY_RATE_MULTIPLIER;
            case NIGHT -> NIGHT_RATE_MULTIPLIER;
            case HOLIDAY -> HOLIDAY_RATE_MULTIPLIER;
            case NIGHT_HOLIDAY -> NIGHT_HOLIDAY_RATE_MULTIPLIER;
        };
    }

}
