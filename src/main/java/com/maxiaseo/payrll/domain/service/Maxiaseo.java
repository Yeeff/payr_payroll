package com.maxiaseo.payrll.domain.service;

import com.maxiaseo.payrll.domain.util.TimeRange;

import java.time.Duration;

public class Maxiaseo {

    public TimeRange validateLunchHour(TimeRange timeRange){
        Long hoursWorkedPerDay = Duration.between(
                timeRange.getStartTime(), timeRange.getEndTime() ).toHours();

        if( hoursWorkedPerDay != 12 ){
            timeRange.setEndTime(timeRange.getEndTime().minusHours(1));
        }

        return timeRange;
    }
}
