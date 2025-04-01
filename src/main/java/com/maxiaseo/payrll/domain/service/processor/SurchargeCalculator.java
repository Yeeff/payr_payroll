package com.maxiaseo.payrll.domain.service.processor;

import com.maxiaseo.payrll.domain.model.Money;
import com.maxiaseo.payrll.domain.model.Surcharge;
import com.maxiaseo.payrll.domain.util.ConstantsDomain;
import com.maxiaseo.payrll.domain.util.ConstantsDomain.SurchargeTypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.maxiaseo.payrll.domain.util.ConstantsDomain.*;

public class SurchargeCalculator {

    private static final Money NIGHT_RATE_MULTIPLIER = new Money("0.35");
    private static final Money HOLIDAY_RATE_MULTIPLIER = new Money("0.75");
    private static final Money NIGHT_HOLIDAY_RATE_MULTIPLIER = new Money("1.1");

    private SurchargeCalculator(){}


    public static List<Surcharge> getSurchargeList(LocalDateTime start, LocalDateTime end, Integer maximumLegalHours, String monthlyPayment) {

        List<Surcharge> surchargesList = new ArrayList<>();

        Surcharge surchargeNight = new Surcharge();
        Surcharge surchargeHoliday = new Surcharge();
        Surcharge surchargeHolidayNight = new Surcharge();

        LocalDateTime current = start.plusMinutes(STEP_IN_MINUTES);

        while (current.isBefore(end) || current.isEqual(end)) {

            if (getSurchargeType(current) == SurchargeTypeEnum.NIGHT) {
                surchargeNight = increaseValueOfStepToSurcharge(surchargeNight, current, SurchargeTypeEnum.NIGHT);
            }
            if (getSurchargeType(current) == SurchargeTypeEnum.HOLIDAY) {
                surchargeHoliday = increaseValueOfStepToSurcharge(surchargeHoliday, current, SurchargeTypeEnum.HOLIDAY);
            }
            if (getSurchargeType(current) == SurchargeTypeEnum.NIGHT_HOLIDAY) {
                surchargeHolidayNight = increaseValueOfStepToSurcharge(surchargeHolidayNight, current, SurchargeTypeEnum.NIGHT_HOLIDAY);
            }

            LocalDateTime maxTimeToCheckSurcharges = start.plusHours(maximumLegalHours );
            if(maxTimeToCheckSurcharges.isEqual(current)) break;

            current = current.plusMinutes(STEP_IN_MINUTES);
        }

        if (surchargeNight.getQuantityOfMinutes() != 0) surchargesList.add(surchargeNight);
        if (surchargeHoliday.getQuantityOfMinutes() != 0) surchargesList.add(surchargeHoliday);
        if (surchargeHolidayNight.getQuantityOfMinutes() != 0) surchargesList.add(surchargeHolidayNight);

        calculateOvertimeCosts(surchargesList, monthlyPayment);

        return surchargesList;
    }

    private static SurchargeTypeEnum getSurchargeType(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        if(time.equals(LocalTime.of(0,0))) {
            dateTime =dateTime.minusMinutes(1);
        }
        DayOfWeek day = dateTime.getDayOfWeek();

        boolean isNightSurcharge = (time.isAfter(NIGHT_START) || time.equals(NIGHT_END)) || time.isBefore(NIGHT_END);
        boolean isSunday = day == DayOfWeek.SUNDAY;

        if (isSunday && isNightSurcharge) {
            return SurchargeTypeEnum.NIGHT_HOLIDAY;
        } else if (isSunday) {
            return SurchargeTypeEnum.HOLIDAY;
        } else if (isNightSurcharge) {
            return SurchargeTypeEnum.NIGHT;
        } else {
            return null;
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


    public static void calculateOvertimeCosts(List<Surcharge> surchargeList, String monthlyPayment) {

        Money baseMonthlyPayment = new Money(monthlyPayment);
        Money legalHoursWorkedPerMonth = new Money(String.valueOf(MAXIMUM_HOURS_PER_WEEK * 5));
        Money baseHourlyRate = baseMonthlyPayment.divide(legalHoursWorkedPerMonth);

        for (Surcharge surcharge : surchargeList) {
            Money overtimeRate = getOvertimeMultiplier(surcharge.getSurchargeTypeEnum());
            Money minutesWorked = new Money(surcharge.getQuantityOfMinutes().toString());
            Money hourlyRate = baseHourlyRate.multiply(overtimeRate);

            Money surchargeCost = hourlyRate.multiply(minutesWorked).divide(new Money(String.valueOf(60)));

            surcharge.setMoneyCost(surchargeCost);
        }
    }
    private static Money getOvertimeMultiplier(ConstantsDomain.SurchargeTypeEnum surchargeTypeEnum) {
        return switch (surchargeTypeEnum) {
            case NIGHT -> NIGHT_RATE_MULTIPLIER;
            case HOLIDAY -> HOLIDAY_RATE_MULTIPLIER;
            case NIGHT_HOLIDAY -> NIGHT_HOLIDAY_RATE_MULTIPLIER;
        };
    }

}
