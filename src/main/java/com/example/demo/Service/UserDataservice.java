package com.example.demo.Service;


import com.example.demo.Repo.Userrepo;
import com.example.demo.model.Housedata;
import com.example.demo.model.Usermodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDataservice {
    @Autowired
    Userrepo userrepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public Usermodel useradd(String username,String password,String email){



        Usermodel obj= userrepo.findByEmail(email);
        if (obj==null) {

            obj = new Usermodel();
            obj.setUsername(username);
            obj.setEmail(email);
            obj.setRole("USER");
            obj.setPassword(bCryptPasswordEncoder.encode(password));

            return userrepo.save(obj);
        }
        else {
            System.out.println("user exist");
            return obj;
        }

    }

}
