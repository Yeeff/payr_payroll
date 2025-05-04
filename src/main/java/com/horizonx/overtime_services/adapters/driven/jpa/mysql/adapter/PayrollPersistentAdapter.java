package com.horizonx.overtime_services.adapters.driven.jpa.mysql.adapter;

import com.horizonx.overtime_services.domain.spi.IPayrollPersistentPort;

import java.time.LocalDate;

public class PayrollPersistentAdapter implements IPayrollPersistentPort {

    @Override
    public Integer getLastHoursWorkedInTheLastWeekByFortnight(LocalDate initDateFortnight) {
        return -1;
    }
}
