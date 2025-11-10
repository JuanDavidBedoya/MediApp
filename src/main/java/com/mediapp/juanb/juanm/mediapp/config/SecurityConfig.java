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

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
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
            // Permitir solicitudes OPTIONS para CORS preflight
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            // 1. Endpoints Públicos
            .requestMatchers("/auth/register", "/auth/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/cities/**", "/eps/**", "/specialities/**", "/doctors/**", "/medications/**").permitAll()

            // 2. Endpoints para USER y DOCTOR (lectura/modificación personal)
            .requestMatchers("/appointments/**").permitAll()
            .requestMatchers("/users/**").hasAnyRole("USER", "DOCTOR")

            // 3. Endpoints exclusivos para DOCTOR (gestión médica)
            .requestMatchers("/auth/register/doctor").hasRole("DOCTOR")
            .requestMatchers("/doctors/**", "/formulas/**", "/formula-details/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.POST, "/cities/**", "/eps/**", "/phones/**", "/user-phones/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.PUT, "/cities/**", "/eps/**", "/phones/**", "/user-phones/**").hasRole("DOCTOR")
            .requestMatchers(HttpMethod.DELETE, "/cities/**", "/eps/**", "/phones/**", "/user-phones/**").hasRole("DOCTOR")

            .requestMatchers(
                    "/doc/swagger-ui/**",  // Para la UI de Swagger (basado en tu URL)
                    "/v3/api-docs/**"      // Para el JSON de la API (el default de SpringDoc)
            ).permitAll()

            // 4. Todo lo demás requiere autenticación
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

}