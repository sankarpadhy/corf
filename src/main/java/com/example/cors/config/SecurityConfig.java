package com.example.cors.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Security Configuration for CORS Protection
 * 
 * Flow Diagram:
 * ┌──────────────────┐     ┌─────────────────┐     ┌────────────────┐
 * │  Client Request  │────▶│  CORS Filter    │────▶│ Origin Check   │
 * └──────────────────┘     └─────────────────┘     └────────────────┘
 *                                                          │
 *                                                          ▼
 * ┌──────────────────┐     ┌─────────────────┐     ┌────────────────┐
 * │   API Response   │◀────│  Add Headers    │◀────│ Config Source  │
 * └──────────────────┘     └─────────────────┘     └────────────────┘
 * 
 * Configuration Steps:
 * 1. Enable CORS at security filter chain level
 * 2. Configure allowed origins, methods, and headers
 * 3. Set up URL patterns for CORS configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean(name = "securityFilterChain")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/api/banking/balance", "/api/banking/transfer").permitAll()  // Allow public access
                .anyRequest().authenticated()
            );
        return http.build();
    }

    /**
     * CORS Configuration Source
     * 
     * Headers Flow:
     * Request Headers        Response Headers
     * ───────────────       ────────────────
     * Origin: domain   ───▶  Access-Control-Allow-Origin
     * Method: POST     ───▶  Access-Control-Allow-Methods
     * Headers: Auth    ───▶  Access-Control-Allow-Headers
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:8080",  // Main banking portal
            "http://localhost:3000",  // Mobile banking (simulated)
            "http://localhost:3001",  // Financial planner (simulated)
            "http://localhost:3002"   // Investment dashboard (simulated)
        ));
        
        // Allow specific methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        
        // Allow specific headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With"
        ));
        
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        
        return source;
    }
}
