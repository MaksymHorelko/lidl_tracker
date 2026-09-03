package com.horelkomaksym.pricetracker.pricetracker.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
@Component
public class UpdateHandler {
    private final MessageHandler messageHandler;

    public SendMessage handleUpdate(Update update) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            String accountName = update.getMessage().getChat().getFirstName();
            String textAnswer = messageHandler.handle(chatId, messageText, accountName);
           return createAnswerMessage(chatId, textAnswer);
    }

    public SendMessage createAnswerMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
}
