package com.example.demo.Controller;


import com.example.demo.Service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteData {
    @Autowired
    DeleteService deleteService;

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        deleteService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @DeleteMapping("/admin/deletepro/{id}")
    public ResponseEntity<String> deletepro(@PathVariable String id) {
        deleteService.deletepropertyById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
