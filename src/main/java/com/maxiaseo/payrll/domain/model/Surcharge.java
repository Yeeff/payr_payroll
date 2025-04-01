package com.maxiaseo.payrll.domain.model;

import com.maxiaseo.payrll.domain.util.ConstantsDomain;
import com.maxiaseo.payrll.domain.util.ConstantsDomain.SurchargeTypeEnum;

import java.time.LocalDateTime;

public class Surcharge {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long quantityOfMinutes;
    private Money moneyCost;
    private SurchargeTypeEnum surchargeTypeEnum;

    public Surcharge() {
        this.quantityOfMinutes = 0L;
    }

    public Surcharge(SurchargeTypeEnum surchargeType, Long quantityOfMinutes) {
        this.surchargeTypeEnum = surchargeType;
        this.quantityOfMinutes = quantityOfMinutes;
    }

    public void increaseValueOfStep(){
        quantityOfMinutes += ConstantsDomain.STEP_IN_MINUTES;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Long getQuantityOfMinutes() {
        return quantityOfMinutes;
    }

    public SurchargeTypeEnum getSurchargeTypeEnum() {
        return surchargeTypeEnum;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Money getMoneyCost() {
        return moneyCost;
    }

    public void setMoneyCost(Money moneyCost) {
        this.moneyCost = moneyCost;
    }

    public void setSurchargeTypeEnum(SurchargeTypeEnum surchargeTypeEnum) {
        this.surchargeTypeEnum = surchargeTypeEnum;
    }
}
