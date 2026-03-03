package com.example.demo.Controller;


import com.example.demo.Service.UserDataservice;
import com.example.demo.model.Usermodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
@CrossOrigin(origins = "https://nestfinderfrontend.vercel.app")

public class UserDatacontroller {

    @Autowired
    UserDataservice userDataservice;
    @PostMapping("/adduser")
    public Usermodel adduser(@RequestParam("username")String username,
                             @RequestParam("password")String password,
                             @RequestParam("email")String email){
        return userDataservice.useradd(username,password,email);

    }
    @GetMapping("/who")
    public Map<String, Object> who() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> response = new HashMap<>();

        if (authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof User) {

            User user = (User) authentication.getPrincipal();
            response.put("who", user.getUsername());

        } else {
            response.put("who", "Guest");
        }

        return response;
    }


}
