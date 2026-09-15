package bd.edu.seu.hellorest.User;

import bd.edu.seu.hellorest.Product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<Product,String> {

}
