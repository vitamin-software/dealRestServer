package io.vitamin.deal.service;

import io.vitamin.deal.domain.DealSummary;
import io.vitamin.deal.domain.FxRate;

import java.math.BigDecimal;

public interface DealReturnService extends DealService {
    BigDecimal getAccumulatedReturn(String clientId);
    BigDecimal getAccumulatedReturn();
    boolean updateFxRate(FxRate fxRate);
}
