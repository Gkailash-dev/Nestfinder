package com.example.demo.Service;

import com.example.demo.Repo.Houserepo;
import com.example.demo.cloud.CloudinaryService;
import com.example.demo.model.Housedata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service

public class Houseservice {
    @Autowired
    Houserepo houseRepo;
@Autowired
    CloudinaryService cloudinaryService;
    public Housedata add(String ownerName, String phoneNumber, String email, String location, String propertyType, Double rent, String description, MultipartFile imageUrls,
                         String advancePayment) {


        try {

            String imageUrl = cloudinaryService.uploadImage(imageUrls);
            Housedata house = new Housedata();

            house.setOwnerName(ownerName);
            house.setPhoneNumber(phoneNumber);
            house.setEmail(email);
            house.setLocation(location);
            house.setPropertyType(propertyType);
            house.setRent(rent);
            house.setDescription(description);
            house.setImageUrls(imageUrl);
            house.setAdvancePayment(advancePayment);
            System.out.println("House save called");
            return houseRepo.save(house);
        }
        catch (IOException e ){
            throw new RuntimeException(e.getMessage());
        }
    }
}
