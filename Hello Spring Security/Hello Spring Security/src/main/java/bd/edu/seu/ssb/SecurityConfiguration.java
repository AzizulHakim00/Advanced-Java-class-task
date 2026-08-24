package bd.edu.seu.ssb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/css/**", "/scss/**", "/images/**", "/libs/**", "/js/**").permitAll()
                        .requestMatchers("/signup", "/signin").permitAll()
                        .requestMatchers("/user").hasRole("USER")    // Allow only USER
                        .requestMatchers("/admin").hasRole("ADMIN")  // Allow only ADMIN
                        .requestMatchers("/profile").hasAnyRole("ADMIN", "USER")  // Allow only ADMIN

                        .requestMatchers("/user/create") .hasAuthority("CREATE_USER")
                        .requestMatchers("/admin/update").hasAuthority("UPDATE_USER")

                        .anyRequest().authenticated() // Allow all authenticated users for other routes
                )
                .formLogin(form -> form
                        .loginPage("/signin")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureUrl("/signin?error=true")
                        .defaultSuccessUrl("/", true) // Redirect to /home on successful login
                )
                .logout(config -> config
                        .logoutSuccessUrl("/signin?logout=true") // Redirect to login page after logout
                        .invalidateHttpSession(true) // Invalidate session after logout
                        .clearAuthentication(true) // Clear authentication
                        .deleteCookies("JSESSIONID") // Delete session cookie
                        .permitAll()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Secure password storage
    }
}
