package com.horelkomaksym.pricetracker.pricetracker.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocDownloader {
    public static Document download(String url) throws IOException {
        for (int i = 0; i < 3; i++) {
            try {
                return Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .timeout(60000)
                        .get();
            } catch (java.net.SocketTimeoutException e) {
                if (i == 2) throw e;
                System.out.println("Timed out for " + url + ", retrying...");
            }
        }
        return null;
    }
}
