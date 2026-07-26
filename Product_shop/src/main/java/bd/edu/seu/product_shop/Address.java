package bd.edu.seu.product_shop;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Embeddable

public class Address {

    @Column( name = "street_Address")
    private String streetAddress;
    private String city;
    private String state;
    private String country;
}
