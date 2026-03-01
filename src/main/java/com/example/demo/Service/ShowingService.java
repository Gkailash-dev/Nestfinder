package com.example.demo.Service;

import com.example.demo.Repo.Houserepo;
import com.example.demo.Repo.Userrepo;
import com.example.demo.model.Housedata;
import com.example.demo.model.Usermodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ShowingService {
    @Autowired
    Houserepo houserepo;
    @Autowired
    Userrepo userrepo;
    public List<Housedata> getall(){
       return houserepo.findAll();

    }

    public List<Usermodel> getalluser() {
       return userrepo.findAll();
    }
}
