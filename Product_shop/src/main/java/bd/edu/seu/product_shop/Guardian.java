package bd.edu.seu.product_shop;


import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor

public class Guardian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
}
