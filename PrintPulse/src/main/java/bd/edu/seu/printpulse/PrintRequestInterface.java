package bd.edu.seu.printpulse;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrintRequestInterface extends JpaRepository<PrintRequest, Integer> {
}
