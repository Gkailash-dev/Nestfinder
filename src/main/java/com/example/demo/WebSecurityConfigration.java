package com.example.demo;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigration {

    @Autowired
    private CustomUserDetailServices userDetailServices;

    @Autowired
    private CoustomOauth2Successhandler coustomOauth2Successhandler;

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())

            .authorizeHttpRequests(auth -> auth
                // Allow preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Public endpoints
                .requestMatchers(
                        "/login",
                        "/adduser",
                        "/",
                        "/all",
                        "/add",
                        "/who",
                        "/search",
                        "/counts",
                        "/admin/getuser"
                ).permitAll()

                // Role-based access
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")

                .anyRequest().authenticated()
            )

            // 🔥 CRITICAL: Return 401 instead of redirect
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("""
                        { "message": "Unauthorized" }
                    """);
                })
            )

            // Form login (no redirect behavior)
            .formLogin(form -> form
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {

                    boolean isAdmin = authentication.getAuthorities()
                            .stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                    response.setStatus(200);
                    response.setContentType("application/json");

                    if (isAdmin) {
                        response.getWriter().write("""
                            { "message": "Login successful", "role": "ADMIN" }
                        """);
                    } else {
                        response.getWriter().write("""
                            { "message": "Login successful", "role": "USER" }
                        """);
                    }
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.getWriter().write("""
                        { "message": "Invalid username or password" }
                    """);
                })
                .permitAll()
            )

            .oauth2Login(oauth -> oauth
                .loginPage("/")
                .successHandler(coustomOauth2Successhandler)
            );

        return http.build();
    }

    // Authentication Provider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailServices);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    // Password Encoder
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS Configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
