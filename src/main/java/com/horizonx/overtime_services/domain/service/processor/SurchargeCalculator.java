package com.horizonx.overtime_services.domain.service.processor;

import com.horizonx.overtime_services.domain.model.Surcharge;
import com.horizonx.overtime_services.domain.util.ConstantsDomain.SurchargeTypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.horizonx.overtime_services.domain.util.ConstantsDomain.*;

public class SurchargeCalculator {

    private SurchargeCalculator(){}


    public static List<Surcharge> getSurchargeList(LocalDateTime start, LocalDateTime end, Integer maximumLegalHours, Boolean isMaximumHoursWorkedPerWeekReached) {

        List<Surcharge> surchargesList = new ArrayList<>();

        Surcharge surchargeNight = new Surcharge();
        Surcharge surchargeHoliday = new Surcharge();
        Surcharge surchargeHolidayNight = new Surcharge();
        Surcharge surchargeOvertimeHolidayNight = new Surcharge();
        Surcharge surchargeOvertimeHoliday = new Surcharge();

        LocalDateTime current = start.plusMinutes(STEP_IN_MINUTES);

        while (current.isBefore(end) || current.isEqual(end)) {

            if (getSurchargeType(current, isMaximumHoursWorkedPerWeekReached) == SurchargeTypeEnum.NIGHT) {
                surchargeNight = increaseValueOfStepToSurcharge(surchargeNight, current, SurchargeTypeEnum.NIGHT);
            }
            if (getSurchargeType(current, isMaximumHoursWorkedPerWeekReached) == SurchargeTypeEnum.HOLIDAY) {
                surchargeHoliday = increaseValueOfStepToSurcharge(surchargeHoliday, current, SurchargeTypeEnum.HOLIDAY);
            }
            if (getSurchargeType(current, isMaximumHoursWorkedPerWeekReached) == SurchargeTypeEnum.NIGHT_HOLIDAY) {
                surchargeHolidayNight = increaseValueOfStepToSurcharge(surchargeHolidayNight, current, SurchargeTypeEnum.NIGHT_HOLIDAY);
            }
            if (getSurchargeType(current, isMaximumHoursWorkedPerWeekReached) == SurchargeTypeEnum.OVERTIME_NIGHT_HOLIDAY) {
                surchargeOvertimeHolidayNight = increaseValueOfStepToSurcharge(surchargeOvertimeHolidayNight, current, SurchargeTypeEnum.OVERTIME_NIGHT_HOLIDAY);
            }
            if (getSurchargeType(current, isMaximumHoursWorkedPerWeekReached) == SurchargeTypeEnum.OVERTIME_HOLIDAY) {
                surchargeOvertimeHoliday = increaseValueOfStepToSurcharge(surchargeOvertimeHoliday, current, SurchargeTypeEnum.OVERTIME_HOLIDAY);
            }

            LocalDateTime maxTimeToCheckSurcharges = start.plusHours(maximumLegalHours );
            if(maxTimeToCheckSurcharges.isEqual(current)) break;

            current = current.plusMinutes(STEP_IN_MINUTES);
        }

        if (surchargeNight.getQuantityOfMinutes() != 0) surchargesList.add(surchargeNight);
        if (surchargeHoliday.getQuantityOfMinutes() != 0) surchargesList.add(surchargeHoliday);
        if (surchargeHolidayNight.getQuantityOfMinutes() != 0) surchargesList.add(surchargeHolidayNight);
        if (surchargeOvertimeHolidayNight.getQuantityOfMinutes() != 0) surchargesList.add(surchargeOvertimeHolidayNight);
        if (surchargeOvertimeHoliday.getQuantityOfMinutes() != 0) surchargesList.add(surchargeOvertimeHoliday);

        return surchargesList;
    }

    private static SurchargeTypeEnum getSurchargeType(LocalDateTime dateTime, Boolean isMaximumHoursWorkedPerWeekReached) {
        LocalTime time = dateTime.toLocalTime();
        if(time.equals(LocalTime.of(0,0))) {
            dateTime =dateTime.minusMinutes(1);
        }

        DayOfWeek day = dateTime.getDayOfWeek();

        LocalDate date = dateTime.toLocalDate();

        boolean isHoliday =  holidays.contains(date);
        boolean isSunday = day == DayOfWeek.SUNDAY;
        boolean isHolidayOrSunday = isSunday || isHoliday;

        boolean isNightSurcharge = (time.isAfter(NIGHT_START) || time.equals(NIGHT_END)) || time.isBefore(NIGHT_END);

        if (isHolidayOrSunday && isNightSurcharge) {

            if(isHoliday) return SurchargeTypeEnum.OVERTIME_NIGHT_HOLIDAY;

            if(isMaximumHoursWorkedPerWeekReached) return SurchargeTypeEnum.OVERTIME_NIGHT_HOLIDAY;
            else return SurchargeTypeEnum.NIGHT_HOLIDAY;

        } else if (isHolidayOrSunday) {

            if(isHoliday) return SurchargeTypeEnum.OVERTIME_HOLIDAY;

            if(isMaximumHoursWorkedPerWeekReached) return SurchargeTypeEnum.OVERTIME_HOLIDAY;
            else return SurchargeTypeEnum.HOLIDAY;

        } else if (isNightSurcharge) {
            return SurchargeTypeEnum.NIGHT;
        } else {
            return SurchargeTypeEnum.DAY;
        }
    }

    private static Surcharge increaseValueOfStepToSurcharge(Surcharge surcharge, LocalDateTime cur, SurchargeTypeEnum type ){
        surcharge.setSurchargeTypeEnum(type);

        if (surcharge.getStart() == null)   {
            surcharge.setStart(cur.minusMinutes(STEP_IN_MINUTES));
        }

        surcharge.setEnd(cur);
        surcharge.increaseValueOfStep();

        return surcharge;
    }

}
