package io.vitamin.deal.domain;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.nevec.rjm.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CompoundInterestRateDeal extends Deal {
    private BigDecimal interestRate; // r
    private int numberOfYear; //t
    private BigDecimal compoundRate; // n

    private final Supplier<BigDecimal> returnValue;

    public CompoundInterestRateDeal(Status status, String clientId, BigDecimal principal, String ccy,
                                    BigDecimal interestRate, int numberOfYear, BigDecimal compoundRate) {
        super(status, clientId, principal, ccy);
        this.interestRate = interestRate;
        this.numberOfYear = numberOfYear;
        this.compoundRate = compoundRate;

        this.returnValue = Suppliers.memoize(this::calculate);
    }

    public CompoundInterestRateDeal() {
        this.returnValue = Suppliers.memoize(this::calculate);
    }

    private BigDecimal calculate() {
        BigDecimal tmp =
                BigDecimalMath.pow(BigDecimal.ONE.add(interestRate.divide(compoundRate, RoundingMode.FLOOR)),
                        interestRate.multiply(new BigDecimal(numberOfYear)));

        return this.getPrincipal().multiply(tmp).add(this.getPrincipal().negate());
    }

    @Override
    public BigDecimal calculate(BigDecimal fxRate) {
        return returnValue.get().multiply(fxRate);
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getNumberOfYear() {
        return numberOfYear;
    }

    public void setNumberOfYear(int numberOfYear) {
        this.numberOfYear = numberOfYear;
    }

    public BigDecimal getCompoundRate() {
        return compoundRate;
    }

    public void setCompoundRate(BigDecimal compoundRate) {
        this.compoundRate = compoundRate;
    }
}
