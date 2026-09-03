package com.horelkomaksym.pricetracker.pricetracker.parser;

import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class LidlParser implements PriceParser {
    @Override
    public BigDecimal extractPrice(String url) throws IllegalArgumentException, IOException {
        if (url.isEmpty()) {
            throw new IllegalArgumentException("Invalid URL");
        }
        var doc = DocDownloader.download(url);
        if (doc == null) {
            throw new IllegalArgumentException("Cannot parse url: " + url);
        }
        Element el = doc.selectFirst("section.buybox-one div.ods-price__value");
        if (el == null) {
            throw new IllegalArgumentException("Cannot parse url: " + url);
        }
        return  new BigDecimal(el.html().replaceAll("[^0-9.]", ""));
    }

    @Override
    public boolean isApplicable(String url) {
        return url.contains(SupportedShops.LIDL.name().toLowerCase());
    }
}
