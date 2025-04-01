package com.maxiaseo.payrll.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {
    private final BigDecimal amount;

    public Money(String amount) {
        BigDecimal tempBigDecimal = new BigDecimal(amount);
        this.amount = tempBigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    public Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }


    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    public Money multiply(Money factor) {
        return new Money(this.amount.multiply(factor.amount));
    }

    public Money divide(Money factor) {
        return new Money(this.amount.divide(factor.amount,2, RoundingMode.HALF_UP));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}