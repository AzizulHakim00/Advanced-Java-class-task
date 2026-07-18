package com.sparktech.hello_spring_boot_7_12_26;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentForm {
    private String name;
    private int id;
    private String email;
    private String phone;
    private String address;
}
