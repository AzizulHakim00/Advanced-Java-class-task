package com.sparktech.shop.student;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student_data")
public class Student {
    @Id
    private int id;
    private String name;
    @Column(name = "mygpa")
    private double gpa;

    @Embedded
    private Address address;

    @ElementCollection
    private List<String> mobileNumbers;

    @OneToOne
    private Guardian guardian;
}
