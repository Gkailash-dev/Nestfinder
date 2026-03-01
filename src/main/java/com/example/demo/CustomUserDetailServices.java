package com.example.demo;

import com.example.demo.Repo.Userrepo;
import com.example.demo.model.Usermodel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailServices implements UserDetailsService {

    private final Userrepo userrepo;

    public CustomUserDetailServices(Userrepo userrepo) {
        this.userrepo = userrepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usermodel user = userrepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found")
                );

        return new CustomUserDetails(user);
    }
}
