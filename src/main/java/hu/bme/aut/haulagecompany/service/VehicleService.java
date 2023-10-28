package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.orElse(null);
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findById(id);

        if (existingVehicle.isPresent()) {
            Vehicle vehicle = existingVehicle.get();
            // Update the fields of the existing vehicle with the values from updatedVehicle
            vehicle.setName(updatedVehicle.getName());
            vehicle.setType(updatedVehicle.getType());
            vehicle.setLicensePlate(updatedVehicle.getLicensePlate());

            return vehicleRepository.save(vehicle);
        } else {
            return null;
        }
    }

    public boolean removeVehicle(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);

        if (vehicle.isPresent()) {
            vehicleRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
