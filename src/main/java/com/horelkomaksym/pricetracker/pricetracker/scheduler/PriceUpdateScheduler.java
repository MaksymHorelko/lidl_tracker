package com.horelkomaksym.pricetracker.pricetracker.scheduler;

import com.horelkomaksym.pricetracker.pricetracker.service.PriceUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceUpdateScheduler {
    private final PriceUpdateService priceUpdateService;

    @Scheduled(cron = "0 0 * * * *")
    public void runPriceUpdateJob() {
        priceUpdateService.updatePrice();
    }
}
