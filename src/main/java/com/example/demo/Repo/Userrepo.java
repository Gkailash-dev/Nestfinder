package com.example.demo.Repo;


import com.example.demo.model.Usermodel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Userrepo extends MongoRepository<Usermodel,String> {
    Optional<Usermodel> findByUsername(String username);

    String deleteById(int id);
     Usermodel findByEmail(String email);

}
