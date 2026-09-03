package com.horelkomaksym.pricetracker.pricetracker.dao;

import com.horelkomaksym.pricetracker.pricetracker.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductDao extends JpaRepository<Product, Long> {
    Optional<Product> findByUrl(String url);
}
