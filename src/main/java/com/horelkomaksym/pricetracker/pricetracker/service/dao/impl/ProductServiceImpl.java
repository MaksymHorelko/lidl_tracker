package com.horelkomaksym.pricetracker.pricetracker.service.dao.impl;

import com.horelkomaksym.pricetracker.pricetracker.dao.ProductDao;
import com.horelkomaksym.pricetracker.pricetracker.model.Product;
import com.horelkomaksym.pricetracker.pricetracker.service.dao.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;

    @Override
    public Optional<Product> findProduct(Long productId) {
        return productDao.findById(productId);
    }

    @Override
    public Product findProduct(String url) {
        return productDao.findByUrl(url).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Product> findAllProducts() {
        return productDao.findAll();
    }

    @Override
    public Product saveProduct(Product product) {
        return productDao.save(product);
    }

    @Override
    public Product saveProduct(String url, BigDecimal currentPrice) {
        return productDao.save(new Product(url, currentPrice));
    }
}
