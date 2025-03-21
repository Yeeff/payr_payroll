package com.maxiaseo.payrll.domain.spi;

import java.time.LocalDate;

public interface IPayrollPersistentPort {

    Integer getLastHoursWorkedInTheLastWeekByFortnight(LocalDate initDateFortnight);
}
