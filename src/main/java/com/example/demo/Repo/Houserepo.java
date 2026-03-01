package com.example.demo.Repo;

import com.example.demo.model.Housedata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface Houserepo extends MongoRepository<Housedata,String > {


    String  deleteById(int id);


    @Query("{ $or: [" +
            "{ 'ownerName': { $regex: ?0, $options: 'i' } }," +
            "{ 'phoneNumber': { $regex: ?0, $options: 'i' } }," +
            "{ 'email': { $regex: ?0, $options: 'i' } }," +
            "{ 'propertyType': { $regex: ?0, $options: 'i' } }," +
            "{ 'location': { $regex: ?0, $options: 'i' } }," +
            "{ 'description': { $regex: ?0, $options: 'i' } }," +
            "{ 'advancePayment': { $regex: ?0, $options: 'i' } }" +
            "] }")
    List<Housedata> searchAll(String keyword);

    long countByPropertyType(String propertyType);
}
