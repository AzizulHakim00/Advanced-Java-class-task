package bd.edu.seu.product_shop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Student_data")
public class Student {

    @Id
    int id;

    String name;

    @Column(name = "mygpa")
    double gpa;

    @Embedded
    private Address address;

    @ElementCollection
    private List<String> mobileNumbeers;


    @Embedded
    @OneToOne
    private Guardian guardian;

}