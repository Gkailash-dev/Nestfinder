package com.example.demo.Controller;

import com.example.demo.Service.Houseservice;
import com.example.demo.model.Housedata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "https://nestfinderfrontend.vercel.app")
public class Controlleraddhouse {
    @Autowired

    Houseservice house_service;
    @PostMapping(value = "/add", consumes = "multipart/form-data")

    public Housedata Add(@RequestParam("ownerName")String ownerName,
                         @RequestParam("phoneNumber")String phoneNumber,
                         @RequestParam("email")String email,
                         @RequestParam("propertyType")String propertyType,
                         @RequestParam("location")String location,
                         @RequestParam("rent")Double rent,
                         @RequestParam("description")String description,
                         @RequestParam("imageUrls") MultipartFile imageUrls,
                         @RequestParam("advancePayment")String  advancePayment

){
        System.out.println("admin/add called");
       return house_service.add(ownerName, phoneNumber, email, location,  // <-- Add location here
               propertyType, rent, description, imageUrls, advancePayment);

    }

    @GetMapping("/k")
    public String hello(){
       System.out.println("helllo");
       return "hello";
    }

}

