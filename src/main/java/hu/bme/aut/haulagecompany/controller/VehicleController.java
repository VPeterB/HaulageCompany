package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.dto.VehicleDTO;
import hu.bme.aut.haulagecompany.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO createdVehicle = vehicleService.createVehicle(vehicleDTO);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        if (vehicle != null) {
            return new ResponseEntity<>(vehicle, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
        if (updatedVehicle != null) {
            return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
