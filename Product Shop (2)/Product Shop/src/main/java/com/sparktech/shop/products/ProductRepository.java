package com.sparktech.shop.products;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // SELECT * FROM product WHERE stock > 10
    List<Product> findAllByStockGreaterThan(int stock);
    // SELECT * FROM product name = ? AND category = ?
    List<Product> findAllByNameEqualsIgnoreCaseAndCategoryEqualsIgnoreCase(String name, String category);
    void deleteAllByCategoryEqualsIgnoreCase(String category);
    long countAllByCategoryEqualsIgnoreCase(String category);
}
