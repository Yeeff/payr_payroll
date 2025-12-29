package com.horizonx.overtime_services.domain.service;

import com.horizonx.overtime_services.domain.util.TimeRange;

import java.time.DayOfWeek;
import java.time.Duration;

import static com.horizonx.overtime_services.domain.util.ConstantsDomain.holidays;

public class Maxiaseo {

    public TimeRange validateLunchHour(TimeRange timeRange){

        DayOfWeek day = timeRange.getStartTime().getDayOfWeek();

        boolean isSunday = day == DayOfWeek.SUNDAY;
        boolean isHoliday = holidays.contains(timeRange.getStartTime().toLocalDate());

        boolean isHolidayOrSunday = isSunday || isHoliday;

        Long hoursWorkedPerDay = Duration.between(
                timeRange.getStartTime(), timeRange.getEndTime() ).toHours();

        if( hoursWorkedPerDay == 12
                || ( hoursWorkedPerDay < 9 && isHolidayOrSunday)
                || (timeRange.getStartTime().getHour() == 10 && timeRange.getEndTime().getHour() == 20 )
        ){
            return timeRange;
        }

        timeRange.setEndTime(timeRange.getEndTime().minusHours(1));

        return timeRange;
    }
}
