package com.horelkomaksym.pricetracker.pricetracker.service.dao;

import com.horelkomaksym.pricetracker.pricetracker.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> findProduct(Long productId);

    Product findProduct(String url);

    List<Product> findAllProducts();

    Product saveProduct(Product product);

    Product saveProduct(String url, BigDecimal currentPrice);
}
