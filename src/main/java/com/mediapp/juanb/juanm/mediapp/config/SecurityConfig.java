package com.mediapp.juanb.juanm.mediapp.config;

import com.mediapp.juanb.juanm.mediapp.config.filters.JwtAuthFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder; 

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder; 
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // 1. Para cualquier persona
            .requestMatchers("/auth/**").permitAll()

            // 2. Para doctores
            .requestMatchers("/formulas/**").hasRole("DOCTOR")
            .requestMatchers("/formula-details/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.POST, "/medicamentos", "/especialidades", "/cities", "/eps", "/phones").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.PUT, "/medicamentos/**", "/especialidades/**", "/cities/**", "/eps/**", "/phones/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.DELETE, "/medicamentos/**", "/especialidades/**", "/cities/**", "/eps/**", "/phones/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.PUT, "/doctors/**", "/users/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.DELETE, "/doctors/**", "/users/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.GET, "/users", "/appointments").hasRole("DOCTOR")

            // 3. Para usuarios
            .requestMatchers(HttpMethod.POST, "/appointments").hasRole("USER")

            //. 4 Para doctores y usuarios
            .requestMatchers(HttpMethod.GET, "/doctors/**", "/medicamentos/**", "/especialidades/**", "/cities/**", "/eps/**", "/phones/**").hasAnyRole("DOCTOR", "USER")
            .requestMatchers(HttpMethod.GET, "/appointments/**", "/users/**").hasAnyRole("DOCTOR", "USER")
            .requestMatchers(HttpMethod.PUT, "/appointments/**").hasAnyRole("DOCTOR", "USER")
            .requestMatchers(HttpMethod.DELETE, "/appointments/**").hasAnyRole("DOCTOR", "USER")
            .requestMatchers("/user-phones/**").hasAnyRole("DOCTOR", "USER")
            

            // 5. Cualquier otra
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
}