package io.vitamin.deal.service;

import io.vitamin.deal.domain.Deal;
import io.vitamin.deal.domain.FxRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Created by vitamin on 16/06/2017.
 */
public class DealReturnServiceImpl implements DealReturnService {

    private static final FxRate DEFAULT_RATE = new FxRate("", BigDecimal.ONE);
    private final DealService dealService;

    private final ConcurrentMap<String, FxRate> fxRates = new ConcurrentHashMap<>();

    public DealReturnServiceImpl(DealService dealService) {
        this.dealService = dealService;
    }

    public boolean updateFxRate(FxRate fxRate){
        fxRates.put(fxRate.getBaseCcy(), fxRate);
        return true;
    }

    @Override
    public BigDecimal getAccumulatedReturn(String clientId) {
        List<Deal> deals = getDealsByClientId(clientId);
        return getAccumulatedReturn(deals);
    }

    @Override
    public BigDecimal getAccumulatedReturn() {
        List<Deal> deals = getDeals();
        return getAccumulatedReturn(deals);
    }

    protected BigDecimal getAccumulatedReturn(List<Deal> deals){
        Map<String, List<Deal>> dealsByCurrency = deals.stream()
                .collect(Collectors.groupingBy(Deal::getCcy));


        BigDecimal accumulated = BigDecimal.ZERO;
        for(Map.Entry<String, List<Deal>> e : dealsByCurrency.entrySet()){
            BigDecimal rate = fxRates.getOrDefault(e.getKey(), DEFAULT_RATE).getRate();
            Optional<BigDecimal> total = e.getValue().stream()
                    .map(deal -> deal.calculate(rate))
                    .reduce(BigDecimal::add);

            accumulated = total.map(accumulated::add).orElse(accumulated);
        }
        return accumulated;
    }

    @Override
    public String add(Deal deal) {
        return dealService.add(deal);
    }

    @Override
    public Optional<Deal> remove(String dealId) {
        return dealService.remove(dealId);
    }

    @Override
    public Optional<Deal> get(String dealId) {
        return dealService.get(dealId);
    }

    @Override
    public List<Deal> getDealsByClientId(String clientId) {
        return dealService.getDealsByClientId(clientId);
    }

    @Override
    public List<Deal> getDeals() {
        return dealService.getDeals();
    }
}
