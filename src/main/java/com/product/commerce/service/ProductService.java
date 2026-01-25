package com.product.commerce.service;

import com.product.commerce.cache.CacheKeys;
import com.product.commerce.dto.ProductRequest;
import com.product.commerce.entity.Product;
import com.product.commerce.repository.ProductRepository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    public ProductService(ProductRepository productRepository, RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
    }

    public Product createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getStock());
        redisTemplate.delete(CacheKeys.PRODUCT_LIST);
        return productRepository.save(product);
    }

    @SuppressWarnings("unchecked")
    public List<Product> getAllProducts() {
    
    String key = CacheKeys.PRODUCT_LIST;

    List<Product> cached = (List<Product>) redisTemplate.opsForValue().get(key);
    if (cached != null) {
        return cached;
    }

    List<Product> products = productRepository.findAll();

    redisTemplate.opsForValue().set(
            key,
            products,
            Duration.ofMinutes(5)
    );

    return products;
    }

    public Product getProductById(Long productId) {

        String key = CacheKeys.PRODUCT + productId;

        // 1️⃣ Try cache
        Product cached = (Product) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        // 2️⃣ DB fallback
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3️⃣ Cache with TTL
        redisTemplate.opsForValue().set(
                key,
                product,
                Duration.ofMinutes(10)
        );

        return product;
    }
}

