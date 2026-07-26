package bd.edu.seu.product_shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    // SELECT * FROM product WHERE stock >= 10

    List<Product> findAllByStockGreaterThanEqual(int stock);

    List<Product> findAllByStockLessThanEqual(int stock);


    // SELECT * FROM product WHERE name = ? AND category = ?

    List<Product> findAllByNameEqualsIgnoreCaseAndCategoryEqualsIgnoreCase(
            String name,
            String category
    );


    List<Product> findDistinctByStockGreaterThanEqual(int stock);

    void deleteAllByCategoryEqualsIgnoreCase(String category);

}