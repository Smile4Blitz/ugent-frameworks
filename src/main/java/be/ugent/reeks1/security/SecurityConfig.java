package be.ugent.reeks1.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
        @Value("{spring.profiles.active}")
        private String ActiveProfile;

        @Value("${admin.username}")
        private String adminUsername;

        @Value("${admin.password}")
        private String adminPassword;

        @Bean
        public UserDetailsService userDetailsService(DataSource dataSource) {
                UserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
                PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

                if (!userDetailsManager.userExists(adminUsername)) {
                        UserDetails admin = User.builder()
                                        .username(adminUsername)
                                        .password(passwordEncoder.encode(adminPassword))
                                        .roles("ADMIN")
                                        .build();
                        userDetailsManager.createUser(admin);
                }
                return userDetailsManager;
        }

        @SuppressWarnings("removal")
        @Profile("prod")
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // authorization
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/h2-console/**")
                                                .permitAll()
                                                .requestMatchers(HttpMethod.POST, "/blogposts/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.PUT, "/blogposts/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(HttpMethod.DELETE, "/blogposts/**")
                                                .hasRole("ADMIN")
                                                .anyRequest()
                                                .permitAll())

                                // CSRF
                                .csrf((csrf) -> csrf
                                                .ignoringRequestMatchers("/h2-console/**")
                                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))

                                // disable frame options (blocked by Firefox)
                                .headers((headers) -> headers
                                                .frameOptions().disable())

                                // enable basic authentication (username/password)
                                .httpBasic();

                return http.build();
        }
}