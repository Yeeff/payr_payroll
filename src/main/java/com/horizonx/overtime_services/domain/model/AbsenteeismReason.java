package com.horizonx.overtime_services.domain.model;

import com.horizonx.overtime_services.domain.util.ConstantsDomain.AbsenceReasonsEnum;

import java.time.LocalDateTime;

public class AbsenteeismReason {

    private LocalDateTime start;
    private LocalDateTime end;
    private Integer quantityOfHours;
    private AbsenceReasonsEnum absenceReasonsEnum;

    public AbsenteeismReason() {
        this.quantityOfHours = 0;
    }

    public AbsenteeismReason(AbsenceReasonsEnum absenceReasons, Integer quantityOfHours) {
        this.absenceReasonsEnum = absenceReasons;
        this.quantityOfHours = quantityOfHours;
    }


    public LocalDateTime getEnd() {
        return end;
    }

    public Integer getQuantityOfHours() {
        return quantityOfHours;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public AbsenceReasonsEnum getAbsenceReasonsEnum() {
        return absenceReasonsEnum;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setQuantityOfHours(Integer quantityOfHours) {
        this.quantityOfHours = quantityOfHours;
    }

    public void setAbsenceReasonsEnum(AbsenceReasonsEnum absenceReasonsEnum) {
        this.absenceReasonsEnum = absenceReasonsEnum;
    }
}
