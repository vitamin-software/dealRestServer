package io.vitamin.deal.service;

import io.vitamin.deal.domain.Deal;

import java.util.List;
import java.util.Optional;

public interface DealService {
    String add(Deal deal);
    Optional<Deal> remove(String dealId);
    Optional<Deal> get(String dealId);
    List<Deal> getDealsByClientId(String clientId);
    List<Deal> getDeals();
}
