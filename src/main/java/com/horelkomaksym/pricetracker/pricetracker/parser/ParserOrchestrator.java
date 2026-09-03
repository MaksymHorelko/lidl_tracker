package com.horelkomaksym.pricetracker.pricetracker.parser;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParserOrchestrator {
    private final List<PriceParser> priceParsers;
    @SneakyThrows
    public BigDecimal getPrice(String url) throws IllegalArgumentException, IOException {
        return priceParsers.stream()
                .filter(parser -> parser.isApplicable(url))
                .findFirst()
                .map(parser -> extractPriceSneaky(parser, url))
                .orElseThrow(() -> new IllegalArgumentException("Shop doesn't supported"));
    }

    @SneakyThrows(IOException.class)
    private BigDecimal extractPriceSneaky(PriceParser parser, String url) {
        return parser.extractPrice(url);
    }
}
