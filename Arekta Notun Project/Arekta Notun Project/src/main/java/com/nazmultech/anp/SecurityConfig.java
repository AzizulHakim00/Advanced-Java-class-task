package com.nazmultech.anp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filer(HttpSecurity http) {
        http.authorizeHttpRequests(request -> request
                        .requestMatchers("/sign-up", "/privacy-policy").permitAll()
                        .requestMatchers("/admin/**").denyAll()
                        .requestMatchers("/payment-*").fullyAuthenticated()
                        .anyRequest().authenticated())
                        .formLogin(form ->
                                form.loginPage("/sign-in")
                                        .usernameParameter("mobile")
                                        .passwordParameter("pin")
                                        .defaultSuccessUrl("/dashboard")
                                        .failureUrl("/sign-in?error=true")
                                    .permitAll()
                        )
                        .logout(Customizer.withDefaults())
                        .rememberMe(Customizer.withDefaults());


        return http.build();
    }

}
