package com.maxiaseo.payrll.adapters.driven.jpa.mysql.adapter;

import com.maxiaseo.payrll.domain.spi.IPayrollPersistentPort;

import java.time.LocalDate;

public class PayrollPersistentAdapter implements IPayrollPersistentPort {

    @Override
    public Integer getLastHoursWorkedInTheLastWeekByFortnight(LocalDate initDateFortnight) {
        return -1;
    }
}
