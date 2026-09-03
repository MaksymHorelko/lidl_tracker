package com.horelkomaksym.pricetracker.pricetracker.service.dao;

import com.horelkomaksym.pricetracker.pricetracker.model.Subscription;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    Optional<Subscription> findSubscription(Long subscriptionId);

    List<Subscription> findSubcriptionByAccount(Long accountId);

    Optional<List<Subscription>> findSubscriptionsReadyForNotify();

    Optional<List<Subscription>> findAll();

    @Transactional
    Subscription saveSubscription(Long accountId, String url, BigDecimal currentPrice, BigDecimal targetPrice);

    @Transactional
    void removeSubscription(Subscription subscription);

    boolean isPresent(Long subscriptionId);
}
