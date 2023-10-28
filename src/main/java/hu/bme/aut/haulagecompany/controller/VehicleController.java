package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fleet")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return vehicles;
    }

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        Vehicle createdVehicle = vehicleService.addVehicle(vehicle);
        return ResponseEntity.status(201).body(createdVehicle);
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (vehicle != null) {
            return ResponseEntity.ok(vehicle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long vehicleId, @RequestBody Vehicle vehicle) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicle);
        if (updatedVehicle != null) {
            return ResponseEntity.ok(updatedVehicle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> removeVehicle(@PathVariable Long vehicleId) {
        boolean success = vehicleService.removeVehicle(vehicleId);
        if (success) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
