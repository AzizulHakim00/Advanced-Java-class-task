package bd.edu.seu.classproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "bd.edu.seu.classproject.focusforge")
public class FocusForgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FocusForgeApplication.class, args);
    }
}
