package io.vitamin.deal.domain;


import java.math.BigDecimal;

public class DealSummary
{
    private final BigDecimal inflow;
    private final BigDecimal outFlow;

    public DealSummary(BigDecimal inflow, BigDecimal outFlow) {
        this.inflow = inflow;
        this.outFlow = outFlow;
    }
}
