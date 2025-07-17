package com.horizonx.overtime_services.domain.service;

import com.horizonx.overtime_services.domain.util.TimeRange;

import java.time.DayOfWeek;
import java.time.Duration;

public class Maxiaseo {

    public TimeRange validateLunchHour(TimeRange timeRange){

        DayOfWeek day = timeRange.getStartTime().getDayOfWeek();

        boolean isSunday = day == DayOfWeek.SUNDAY;
        Long hoursWorkedPerDay = Duration.between(
                timeRange.getStartTime(), timeRange.getEndTime() ).toHours();

        if( hoursWorkedPerDay == 12
                || ( hoursWorkedPerDay < 9 && isSunday)
                || (timeRange.getStartTime().getHour() == 10 && timeRange.getEndTime().getHour() == 20 )
        ){
            return timeRange;
        }

        timeRange.setEndTime(timeRange.getEndTime().minusHours(1));

        return timeRange;
    }
}
