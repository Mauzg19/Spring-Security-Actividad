package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // SIN autenticación
                .requestMatchers(HttpMethod.GET, "/api/usuarios").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/params").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // CON autenticación
                .requestMatchers(HttpMethod.POST, "/api/usuarios").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").authenticated()

                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // para H2

        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User
                .withUsername("admin")
                .password("{noop}1234")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

