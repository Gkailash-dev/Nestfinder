package com.example.demo.Controller;

import com.example.demo.Repo.Houserepo;
import com.example.demo.Service.ShowingService;
import com.example.demo.model.Housedata;
import com.example.demo.model.Usermodel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ShowingController {
    @Autowired
    ShowingService showingService;
@Autowired
    Houserepo houserepo;
    @GetMapping("/all")
    public List<Housedata> getall() {
        return showingService.getall();

    }
    @GetMapping("/admin/getuser")

        public List<Usermodel> getalluser(){
            return showingService.getalluser();

    }

    @GetMapping("/search")
    public List<Housedata> search(@RequestParam String keyword) {
        return houserepo.searchAll(keyword);
    }



    @GetMapping("/counts")
    public Map<String, Long> getCounts() {

        long houses = houserepo.countByPropertyType("house");
        long parking = houserepo.countByPropertyType("parking");
        long shop = houserepo.countByPropertyType("shop");

        Map<String, Long> result = new HashMap<>();
        result.put("houses", houses);
        result.put("parking", parking);
        result.put("shop",shop);


        return result;
    }


}
