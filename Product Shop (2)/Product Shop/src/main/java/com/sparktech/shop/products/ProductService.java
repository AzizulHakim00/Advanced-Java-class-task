package com.sparktech.shop.products;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void saveProduct(Product product) {
        if (product.getId() == 404) {
            System.out.println("Product not found");
            return;
        }

        productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAllByStockGreaterThan(10);
    }

    public Product getById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteById(int id) {
        productRepository.deleteById(id);
    }


}
