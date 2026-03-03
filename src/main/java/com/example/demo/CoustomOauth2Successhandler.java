package com.example.demo;

import com.example.demo.Repo.Userrepo;
import com.example.demo.model.Usermodel;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class CoustomOauth2Successhandler implements AuthenticationSuccessHandler {

    private final Userrepo userrepo;

    public CoustomOauth2Successhandler(Userrepo userrepo) {
        this.userrepo = userrepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        if (email == null) {
            throw new RuntimeException("Email not provided by OAuth provider");
        }

        email = email.toLowerCase().trim();

        String message;
        Usermodel user = userrepo.findByEmail(email);

        System.out.println("User found: " + user);

        // Create new user if not exists
        if (user == null) {
            user = new Usermodel();
            user.setUsername(name != null ? name : email);
            user.setEmail(email);
            user.setRole("ROLE_USER");

            userrepo.save(user);
            System.out.println("New user created");

            message = "New user created";
        } else {
            message = "Welcome back!";
        }

        // Create authentication object
        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        Collections.singletonList(
                                new SimpleGrantedAuthority(user.getRole()))
                );

        // Set authentication
        SecurityContextHolder.getContext().setAuthentication(auth);

        // ✅ Save authentication into session
        request.getSession(true).setAttribute(
                "SPRING_SECURITY_CONTEXT",
                SecurityContextHolder.getContext()
        );

        // Redirect based on role
        if ("ROLE_ADMIN".equals(user.getRole())) {
            response.sendRedirect("https://nestfinderfrontend.vercel.app/admin/dashboard");
        } else {
            response.sendRedirect(
                    "https://nestfinderfrontend.vercel.app/user/user_product?message=" +
                            URLEncoder.encode(message, StandardCharsets.UTF_8)
            );
        }
    }
}
