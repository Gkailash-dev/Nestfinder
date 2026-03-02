package com.example.demo;

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
    CoustomOauth2Successhandler coustomOauth2Successhandler;
    @Autowired
    private CustomUserDetailServices userDetailServices;

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                // 🔐 ROLE-BASED ACCESS (SPRING SIDE)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/adduser", "/","/all","/add","/who","/search","/counts","/admin/getuser").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")

                        .anyRequest().authenticated()
                )

                // 🔑 LOGIN CONFIG
                .formLogin(form -> form
                        .loginProcessingUrl("/login")

                        // ✅ SUCCESS → JSON RESPONSE
                        .successHandler((request, response, authentication) -> {

                            boolean isAdmin = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                            response.setStatus(200);
                            response.setContentType("application/json");
                            System.out.println(authentication.getAuthorities());
                            System.out.println(authentication.getAuthorities());
                            if (isAdmin) {
                                response.getWriter().write("""
                            {
                              "message": "Login successful",
                              "role": "ADMIN"
                            }
                        """);
                            } else {
                                response.getWriter().write("""
                            {
                              "message": "Login successful",
                              "role": "USER"
                            }
                        """);
                            }
                        })

                        // ❌ FAILURE → JSON ERROR
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                        {
                          "message": "Invalid username or password"
                        }
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

    // 🔐 AUTH PROVIDER
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailServices);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    // 🔑 PASSWORD ENCODER
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



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
