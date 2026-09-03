package com.horelkomaksym.pricetracker.pricetracker.service.dao.impl;

import com.horelkomaksym.pricetracker.pricetracker.dao.SubscriptionDao;
import com.horelkomaksym.pricetracker.pricetracker.model.Account;
import com.horelkomaksym.pricetracker.pricetracker.model.Product;
import com.horelkomaksym.pricetracker.pricetracker.model.Subscription;
import com.horelkomaksym.pricetracker.pricetracker.service.dao.AccountService;
import com.horelkomaksym.pricetracker.pricetracker.service.dao.ProductService;
import com.horelkomaksym.pricetracker.pricetracker.service.dao.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionDao subscriptionDao;
    private final AccountService accountService;
    private final ProductService productService;

    @Override
    public Optional<Subscription> findSubscription(Long subscriptionId) {
        return subscriptionDao.findById(subscriptionId);
    }

    @Override
    public List<Subscription> findSubcriptionByAccount(Long accountId) {
        return subscriptionDao.findByAccount_AccountId(accountId);
    }

    @Override
    public Optional<List<Subscription>>  findSubscriptionsReadyForNotify() {
        return Optional.of(subscriptionDao.findSubscriptionsReadyForNotify());
    }

    @Override
    public Optional<List<Subscription>> findAll() {
        return Optional.of(subscriptionDao.findAll());
    }

    @Transactional
    @Override
    public Subscription saveSubscription(Long accountId, String url, BigDecimal currentPrice, BigDecimal targetPrice) {
        Account account = accountService.findAccount(accountId).orElseThrow(() -> new IllegalArgumentException("Account wasn't found"));
        Product product;
        try {
            product = productService.findProduct(url);
        } catch (IllegalArgumentException il) {
            product = new Product(url, currentPrice);
            productService.saveProduct(product);
        }
        return subscriptionDao.save(new Subscription(targetPrice, product, account));
    }

    @Override
    public void removeSubscription(Subscription subscription) {
        subscriptionDao.delete(subscription);
    }

    @Override
    public boolean isPresent(Long subscriptionId) {
        return subscriptionDao.findById(subscriptionId).isPresent();
    }
}
