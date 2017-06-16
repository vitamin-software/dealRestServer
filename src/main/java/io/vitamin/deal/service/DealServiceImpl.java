package io.vitamin.deal.service;

import io.vitamin.deal.domain.Deal;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DealServiceImpl implements DealService {

    private final Map<String, Deal> deals;
    private final Map<String, Set<Entity<Deal>>> dealsByClient;
    private final Supplier<Long> idGenerator;

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    public DealServiceImpl(Supplier<Long> idGenerator) {
        deals = new HashMap<>(); // TreeMap if it is not JAVA 8
        dealsByClient = new HashMap<>();
        this.idGenerator = idGenerator;
    }

    @Override
    public String add(Deal deal) {
        writeLock.lock();
        try{
            Long id = idGenerator.get();
            deals.put(id.toString(), deal);
            Set<Entity<Deal>> dealsForClient = dealsByClient.computeIfAbsent(deal.getClientId(), c -> new HashSet<>());
            dealsForClient.add(new Entity<>(deal, id.toString()));
            return id.toString();
        }finally {
            writeLock.unlock();
        }
    }


    @Override
    public Optional<Deal> get(String dealId) {
        readLock.lock();
        try {
            return Optional.ofNullable(deals.get(dealId));
        }finally {
            readLock.unlock();
        }
    }

    @Override
    public Optional<Deal> remove(String dealId) {
        writeLock.lock();
        try{
            Deal deal = deals.remove(dealId);
            if(deal != null){
                dealsByClient.get(deal.getClientId()).remove(new Entity<>(deal, dealId));
            }

            return Optional.ofNullable(deal);
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    public List<Deal> getDealsByClientId(String clientId) {
        readLock.lock();
        try {
            Set<Entity<Deal>> deals = dealsByClient.getOrDefault(clientId, Collections.emptySet());
            return deals.stream()
                    .map(Entity::getData)
                    .collect(Collectors.toList());
        }finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Deal> getDeals() {
        readLock.lock();
        try {
            return new ArrayList<>(deals.values());
        }finally {
            readLock.unlock();
        }
    }

    private static class Entity<T> {
        private final T t;
        private final String id;

        public Entity(T t, String id) {
            this.t = t;
            this.id = id;
        }


        public T getData() {
            return t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entity<?> entity = (Entity<?>) o;
            return Objects.equals(id, entity.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
