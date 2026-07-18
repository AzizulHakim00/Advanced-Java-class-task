package com.sparktech.hello_spring_boot_7_12_26;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
}
