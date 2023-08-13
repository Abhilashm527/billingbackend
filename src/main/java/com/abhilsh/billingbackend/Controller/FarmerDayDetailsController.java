package com.abhilsh.billingbackend.Controller;

import com.abhilsh.billingbackend.Model.FarmerDayData;
import com.abhilsh.billingbackend.Repository.FarmerDayImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
public class FarmerDayDetailsController {

    @Autowired
    private FarmerDayImpl farmerDayImpl;

    @CrossOrigin(origins = "https://enchanting-tiramisu-ddf680.netlify.app/")
    @PostMapping("/addFarmerDaydetails")
    public ResponseEntity addFarmer(@RequestBody FarmerDayData farmerDayData) throws SQLException {
         FarmerDayData farmerDayData1 =farmerDayImpl.createFramerDaydetails(farmerDayData);
        return ResponseEntity.ok(farmerDayData1);
    }
    @GetMapping("/getFarmerDetailsById/{id}")
    public ResponseEntity addFarmer(@PathVariable String id) throws SQLException {
        List<LinkedHashMap<String,Object>> farmerDayData1 =farmerDayImpl.getFarmerDetailsById(id);
        if(farmerDayData1 == null || farmerDayData1.isEmpty()) {
            return ResponseEntity.status(404).body("No Data found ");
        }
        return ResponseEntity.ok(farmerDayData1);
    }

    @GetMapping("/getFarmerAllFarmersDetails")
    public ResponseEntity getFarmerAllfarmersdata() throws SQLException {
        List<LinkedHashMap<String,Object>> farmerDayData1 =farmerDayImpl.getFarmerAllFarmersDetails();
        if(farmerDayData1 == null || farmerDayData1.isEmpty()) {
            return ResponseEntity.status(404).body("No Data found ");
        }
        return ResponseEntity.ok(farmerDayData1);
    }

}
