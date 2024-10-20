package be.ugent.reeks1.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Profile("test")
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
        @SuppressWarnings("removal")
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // authorization
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/h2-console/**").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/blogposts/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/blogposts/**").hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/blogposts/**").hasRole("ADMIN")
                                                .anyRequest().permitAll())

                                // CSRF
                                .csrf().disable()

                                // disable frame options (blocked by Firefox)
                                .headers((headers) -> headers
                                                .frameOptions().disable())

                                // enable basic authentication (username/password)
                                .httpBasic();

                return http.build();
        }
}
