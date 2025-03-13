package com.maxiaseo.payrll.adapters.driving.http.dto;

import com.maxiaseo.payrll.domain.model.AbsenteeismReason;
import com.maxiaseo.payrll.domain.model.Overtime;
import com.maxiaseo.payrll.domain.model.OvertimeSurcharge;
import com.maxiaseo.payrll.domain.model.Surcharge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeResponseDto {

    private Long id;
    private String name;
    private List<Surcharge> surcharges;
    private List<Overtime> overtimes;
    private List<OvertimeSurcharge> overtimeSurcharges;
    private List<AbsenteeismReason> absenteeismReasons;

    private Double totalOvertimeSurchargeHoursNightHoliday;
    private Double totalOvertimeSurchargeHoursHoliday;
    private Double totalSurchargeHoursHoliday;
    private Double totalOvertimeHoursDay;
    private Double totalOvertimeHoursNightHoliday;
    private Double totalSurchargeHoursNightHoliday;
    private Double totalSurchargeHoursNight;
    private Double totalOvertimeHoursHoliday;
    private Double totalOvertimeHoursNight;

}
