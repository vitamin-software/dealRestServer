package io.vitamin.deal.domain;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.math.BigDecimal;

public class SimpleInterestRateDeal extends Deal{

    private int numberOfYear;
    private BigDecimal interestRate;

    private final Supplier<BigDecimal> returnValue;

    public SimpleInterestRateDeal(Status status, String clientId, BigDecimal
            principal, String ccy, BigDecimal interestRate, int numberOfYear) {
        super(status, clientId, principal, ccy);

        this.interestRate = interestRate;
        this.numberOfYear = numberOfYear;
        this.returnValue = Suppliers.memoize(this::calculate);
    }

    public SimpleInterestRateDeal() {
        this.returnValue = Suppliers.memoize(this::calculate);
    }

    private BigDecimal calculate() {
        return this.getPrincipal().multiply(interestRate).multiply(new BigDecimal(numberOfYear));
    }

    @Override
    public BigDecimal calculate(BigDecimal fxRate) {
        return returnValue.get().multiply(fxRate);
    }

    public int getNumberOfYear() {
        return numberOfYear;
    }

    public void setNumberOfYear(int numberOfYear) {
        this.numberOfYear = numberOfYear;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
