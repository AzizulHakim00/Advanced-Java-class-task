package bd.edu.seu.printpulse;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrintRequestInterface extends MongoRepository<PrintRequest, Integer> {
}
