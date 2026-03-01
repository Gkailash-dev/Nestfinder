package com.example.demo.Service;


import com.example.demo.Repo.Houserepo;
import com.example.demo.Repo.Userrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteService {
    @Autowired
    Userrepo userrepo;

    @Autowired
    Houserepo houserepo;
    public void deleteUserById(String id) {
        if (!userrepo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userrepo.deleteById(id);
    }




    public void deletepropertyById(String id) {
        if (!houserepo.existsById(id)) {
            throw new RuntimeException("prpperty not found");
        }
        houserepo.deleteById(id);
    }
}
