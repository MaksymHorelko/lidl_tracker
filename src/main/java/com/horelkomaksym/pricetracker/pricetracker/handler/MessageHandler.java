package com.horelkomaksym.pricetracker.pricetracker.handler;

import com.horelkomaksym.pricetracker.pricetracker.handler.commands.BotCommands;
import com.horelkomaksym.pricetracker.pricetracker.model.Account;
import com.horelkomaksym.pricetracker.pricetracker.model.Product;
import com.horelkomaksym.pricetracker.pricetracker.model.Subscription;
import com.horelkomaksym.pricetracker.pricetracker.parser.ParserOrchestrator;
import com.horelkomaksym.pricetracker.pricetracker.parser.SupportedShops;
import com.horelkomaksym.pricetracker.pricetracker.service.AccountState;
import com.horelkomaksym.pricetracker.pricetracker.service.dao.AccountService;
import com.horelkomaksym.pricetracker.pricetracker.service.dao.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler {
    private final SubscriptionService subscriptionService;
    private final AccountService accountService;
    private final ParserOrchestrator parserOrchestrator;

    private static final String HELLO_COMMAND = "Hello!";
    private static final String ADD_COMMAND = "Please, send product url.";
    private static final String UNKNOWN_COMMAND = "Unknown command.";
    private static final String SEND_PRICE = "Now, send target price for this product.";
    private static final String ERROR_URL = "Check provided URL.";
    private static final String ERROR_PRICE = "Check provided number.";
    private static final String ERROR_DOWNLOAD = "There was a download problem, try again.";
    private static final String ERROR_GENERAL = "An unexpected error occurred. Please try again.";
    private static final String ERROR_SESSION_EXPIRED = "Session expired. Please start again with /add.";

    private final Map<Long, String> products = new ConcurrentHashMap<>();

    public String handle(Long chatId, String text, String accountName) {
        if (isCommand(text)) {
            accountService.changeAccountState(chatId, AccountState.FREE.toString());
            products.remove(chatId);
            return handleCommand(chatId, text);
        }

        Account account = accountService.findOrSaveAccount(chatId, accountName);
        AccountState accountState = AccountState.valueOf(account.getAccountState());

        return switch (accountState) {
            case FREE -> handleCommand(chatId, text);
            case WAITING_FOR_URL -> handleUrlInput(chatId, text);
            case WAITING_FOR_PRICE -> handlePriceInput(chatId, text);
        };
    }

    private String handleUrlInput(Long chatId, String text) {
        try {
            URI uri = new URI(text);
            URI cleanUri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, null);

            products.put(chatId, cleanUri.toString());
            accountService.changeAccountState(chatId, AccountState.WAITING_FOR_PRICE.toString());
            return SEND_PRICE;
        } catch (URISyntaxException e) {
            log.warn("Invalid URI syntax from user {}: {}", chatId, text);
            return ERROR_URL;
        }
    }

    private String handlePriceInput(Long chatId, String text) {
        String url = products.remove(chatId);

        if (url == null) {
            accountService.changeAccountState(chatId, AccountState.FREE.toString());
            return ERROR_SESSION_EXPIRED;
        }

        try {
            BigDecimal targetPrice = new BigDecimal(text);
            BigDecimal currentPrice = parserOrchestrator.getPrice(url);

            subscriptionService.saveSubscription(chatId, url, currentPrice, targetPrice);
            return String.format("We will inform you when the product gets a discount.\nCurrent price: %s, target price: %s",
                    currentPrice, targetPrice);

        } catch (NumberFormatException e) {
            products.put(chatId, url);
            return ERROR_PRICE;
        } catch (IllegalArgumentException e) {
            accountService.changeAccountState(chatId, AccountState.FREE.toString());
            return e.getMessage();
        } catch (IOException e) {
            log.error("Download error for URL: {}", url, e);
            accountService.changeAccountState(chatId, AccountState.FREE.toString());
            return ERROR_DOWNLOAD;
        } catch (Exception e) {
            log.error("Unexpected error during price check for user {}", chatId, e);
            accountService.changeAccountState(chatId, AccountState.FREE.toString());
            return ERROR_GENERAL;
        }
    }

    private String handleCommand(Long chatId, String text) {
        BotCommands option = BotCommands.valueOfOption(text);
        return switch (option) {
            case START -> HELLO_COMMAND;
            case ADD -> {
                accountService.changeAccountState(chatId, AccountState.WAITING_FOR_URL.toString());
                yield ADD_COMMAND;
            }
            case LIST -> executeListCommand(chatId);
            case SHOPS -> executeShopsCommand();
            default -> UNKNOWN_COMMAND;
        };
    }

    private String executeShopsCommand() {
        return Arrays.stream(SupportedShops.values())
                .map(SupportedShops::toString)
                .collect(Collectors.joining(", "));
    }

    private String executeListCommand(Long chatId) {
        List<Subscription> list = subscriptionService.findSubcriptionByAccount(chatId);

        if (list == null || list.isEmpty()) {
            return "You don't have any products in your list.";
        }

        return list.stream().map(sub -> {
            Product pr = sub.getProduct();
            return String.format("URL: %s\nCurrent product price: %s\nTarget price: %s\n",
                    pr.getUrl(), pr.getCurrentPrice(), sub.getTargetPrice());
        }).collect(Collectors.joining("\n"));
    }

    private boolean isCommand(String text) {
        return text != null && text.startsWith("/");
    }
}