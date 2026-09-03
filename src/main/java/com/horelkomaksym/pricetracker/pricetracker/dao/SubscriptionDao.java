package com.horelkomaksym.pricetracker.pricetracker.dao;

import com.horelkomaksym.pricetracker.pricetracker.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionDao extends JpaRepository<Subscription, Long> {
    List<Subscription> findByAccount_AccountId(Long accountId);

    @Query("SELECT s FROM Subscription s " +
            "JOIN FETCH s.product p " +
            "JOIN FETCH s.account a " +
            "WHERE s.targetPrice >= p.currentPrice")
    List<Subscription> findSubscriptionsReadyForNotify();
}
