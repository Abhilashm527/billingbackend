package com.abhilsh.billingbackend.Controller;

import com.abhilsh.billingbackend.Model.Farmer;
import com.abhilsh.billingbackend.Repository.FarmerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class FarmerController {

    @Autowired
    private FarmerImpl farmerImpl;

    @CrossOrigin(origins = "https://enchanting-tiramisu-ddf680.netlify.app/")
    @PostMapping("/addFarmer")
    public ResponseEntity addFarmer(@RequestBody Farmer farmer) throws SQLException {
      Farmer farmer1 = farmerImpl.create(farmer);
      return ResponseEntity.ok(farmer1);
    }
    @GetMapping("/getFarmer/{id}")
    public ResponseEntity addFarmer(@PathVariable String id) throws SQLException {
        Farmer farmer1 = farmerImpl.getFarmer(id);
        if(farmer1 == null)
            return ResponseEntity.status(404).body("Not found");
        return ResponseEntity.ok(farmer1);
    }
    @DeleteMapping("/deleteFarmer/{id}")
    public ResponseEntity deleteFarmer(@PathVariable String id) throws SQLException {
        Boolean isdeleted = farmerImpl.delete(id);
        if(!isdeleted)
            return ResponseEntity.status(404).body("Not found");
        return ResponseEntity.ok("The farmer details has been deleted ");
    }
    @PutMapping("/updateFarmer/{id}")
    public ResponseEntity updateFarmer(@PathVariable String id,@RequestBody Farmer farmer) throws SQLException {
        Farmer farmer1 = farmerImpl.updateFarmer(id,farmer);
        return ResponseEntity.ok(farmer1);
    }
    @GetMapping("/getAllFarmers")
    public ResponseEntity getAllFarmers() throws SQLException {
        List<Farmer> farmer1 = farmerImpl.getAllFarmers();
        return ResponseEntity.ok(farmer1);
    }

}
