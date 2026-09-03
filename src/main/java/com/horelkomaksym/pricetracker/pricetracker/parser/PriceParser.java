package com.horelkomaksym.pricetracker.pricetracker.parser;

import java.io.IOException;
import java.math.BigDecimal;

public interface PriceParser {
    BigDecimal extractPrice(String url) throws IllegalArgumentException, IOException;

    boolean isApplicable(String url);
}
