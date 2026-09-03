package com.horelkomaksym.pricetracker.pricetracker.bot;
import com.horelkomaksym.pricetracker.pricetracker.handler.UpdateHandler;
import com.horelkomaksym.pricetracker.pricetracker.notification.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class Bot implements LongPollingUpdateConsumer, NotificationService {
    private final TelegramClient telegramClient;
    private final UpdateHandler updateHandler;

    @Override
    public void consume(List<Update> list) {
        list.forEach(this::handleSingleUpdate);
    }

    public void handleSingleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var result = updateHandler.handleUpdate(update);
            sendAnswer(result);
        }
    }

    private void sendAnswer(SendMessage message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    @Override
    public void sendPriceDropNotification(Long chatId, String url, BigDecimal newPrice, BigDecimal oldPrice) {
        var notification = updateHandler.createAnswerMessage(chatId, String.format("The product is cheaper now: %s, but was: %s\nURL: %s", newPrice,oldPrice,url));
        sendAnswer(notification);
    }
}