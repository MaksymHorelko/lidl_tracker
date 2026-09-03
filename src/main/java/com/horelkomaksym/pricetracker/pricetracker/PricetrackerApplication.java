package com.horelkomaksym.pricetracker.pricetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PricetrackerApplication  {
	public static void main(String[] args) {
		SpringApplication.run(PricetrackerApplication.class, args);
	}
}
