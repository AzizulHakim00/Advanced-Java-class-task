package com.safayet.afsos_nama.model;

import com.safayet.afsos_nama.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 150)
    private String university;

    @Column(length = 100)
    private String department;

    @Column(length = 50)
    private String currentSemester;

    @Column(precision = 4, scale = 2)
    private BigDecimal currentCgpa;

    @Column(precision = 4, scale = 2)
    private BigDecimal targetCgpa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.STUDENT;

    @Column(nullable = false)
    private boolean enabled = true;
}
