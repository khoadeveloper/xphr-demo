package com.kenny.xphrdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("123456789"))  // <-- Encode the password
                .roles("ADMIN")
                .build();

        UserDetails employee = User.withUsername("employee")
                .password(encoder.encode("123456789"))  // <-- Encode the password
                .roles("EMPLOYEE")
                .build();

        UserDetails auditor = User.withUsername("auditor")
                .password(encoder.encode("123456789"))  // <-- Encode the password
                .roles("AUDITOR")
                .build();

        return new InMemoryUserDetailsManager(admin, employee, auditor);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/report").authenticated()
                        .requestMatchers("/**").permitAll()
                )
                .formLogin(withDefaults());

        return http.build();
    }
}
