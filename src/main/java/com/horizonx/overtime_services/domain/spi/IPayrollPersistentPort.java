package com.horizonx.overtime_services.domain.spi;

import java.time.LocalDate;

public interface IPayrollPersistentPort {

    Integer getLastHoursWorkedInTheLastWeekByFortnight(LocalDate initDateFortnight);
}
