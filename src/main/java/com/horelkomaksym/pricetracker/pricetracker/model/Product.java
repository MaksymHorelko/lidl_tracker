package com.horelkomaksym.pricetracker.pricetracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor

@Table(name = "product")
public class Product {
    public Product(@NotNull String url, @NotNull BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
        this.url = url;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String url;

    @NotNull
    @Column(name = "current_price")
    private BigDecimal currentPrice;
}
