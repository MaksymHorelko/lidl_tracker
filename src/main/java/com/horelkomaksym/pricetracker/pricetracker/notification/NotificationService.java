package com.horelkomaksym.pricetracker.pricetracker.notification;

import java.math.BigDecimal;

public interface NotificationService {
    void sendPriceDropNotification(Long chatId, String url, BigDecimal newPrice, BigDecimal oldPrice);
}
