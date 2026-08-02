package bd.edu.seu.classproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "bd.edu.seu.classproject.focusforge")
@EntityScan(basePackages = "bd.edu.seu.classproject.focusforge")
@EnableJpaRepositories(basePackages = "bd.edu.seu.classproject.focusforge")
public class FocusForgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FocusForgeApplication.class, args);
    }
}
