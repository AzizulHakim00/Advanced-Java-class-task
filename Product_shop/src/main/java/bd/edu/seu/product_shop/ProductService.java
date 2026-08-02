package bd.edu.seu.product_shop;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void saveProduct(Product product){
        if (product.getId() == null){
            System.out.println("product id is null");
            return;
        }

        productRepository.save(product);
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product getById(int id){
        return productRepository.findById(id).orElse(null);
    }

    public void deleteById(int id){
        productRepository.deleteById(id);
    }
}