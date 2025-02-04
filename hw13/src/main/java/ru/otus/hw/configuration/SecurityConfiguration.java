package ru.otus.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(antMatcher("/books")).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(antMatcher("/comments")).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(antMatcher("/books/new")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(antMatcher("/books/edit/*")).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(antMatcher("/books/remove/*")).hasAuthority("ROLE_ADMIN")
                        .anyRequest().hasAnyAuthority("ROLE_USER", "ROLE_ADMIN"))
                .formLogin(Customizer.withDefaults());
        return http.build();
    }
}
