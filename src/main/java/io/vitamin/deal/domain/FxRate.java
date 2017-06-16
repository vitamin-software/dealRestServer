package io.vitamin.deal.domain;

import java.math.BigDecimal;

public class FxRate implements Validable{
    private String baseCcy;
    private BigDecimal rate;

    public FxRate(String baseCcy, BigDecimal rate) {
        this.baseCcy = baseCcy;
        this.rate = rate;
    }

    public FxRate() {}

    public String getBaseCcy() {
        return baseCcy;
    }

    public void setBaseCcy(String baseCcy) {
        this.baseCcy = baseCcy;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
