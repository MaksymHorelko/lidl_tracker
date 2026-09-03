package com.horelkomaksym.pricetracker.pricetracker.service;

import com.horelkomaksym.pricetracker.pricetracker.model.Product;
import com.horelkomaksym.pricetracker.pricetracker.model.Subscription;
import com.horelkomaksym.pricetracker.pricetracker.notification.NotificationService;
import com.horelkomaksym.pricetracker.pricetracker.parser.ParserOrchestrator;
import com.horelkomaksym.pricetracker.pricetracker.service.dao.ProductService;
import com.horelkomaksym.pricetracker.pricetracker.service.dao.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class PriceUpdateService {
    private final ParserOrchestrator parserOrchestrator;
    private final SubscriptionService subscriptionService;
    private final ProductService productService;
    private final NotificationService notificationService;

    public void updatePrice() {
        List<Product> products = productService.findAllProducts();
        List<CompletableFuture<Void>> futures = products.stream().map(product ->
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return parserOrchestrator.getPrice(product.getUrl());
                    } catch (Exception e) {
                        log.error("Parsing error for {}: {}", product.getUrl(), e.getMessage());
                        return null;
                    }
                }).thenAccept(newPrice -> {
                    if (newPrice != null) {
                        processPriceUpdate(product, newPrice);
                    }
                })
        ).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Transactional
    private void processPriceUpdate(Product product, BigDecimal newPrice) {
        System.out.println("!");
        BigDecimal oldPrice = product.getCurrentPrice();
        int compare = newPrice.compareTo(oldPrice);
        if (compare != 0) {
            product.setCurrentPrice(newPrice);
            productService.saveProduct(product);
            if (compare < 0) {
                List<Subscription> subscriptions = subscriptionService.findSubscriptionsReadyForNotify().orElseGet(ArrayList::new);
                for (Subscription sub : subscriptions) {
                    notificationService.sendPriceDropNotification(sub.getAccount().getAccountId(), product.getUrl(), product.getCurrentPrice(), oldPrice);
                    subscriptionService.removeSubscription(sub);
                }
            }
        }
    }
}
