package bd.edu.seu.product_shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name="Student_data")

public class Student {

    @Id
    int id ;

    String name;

    @Column(name = "my_gpa" )
    double gpa;
}
