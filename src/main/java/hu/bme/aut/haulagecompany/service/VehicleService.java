package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.model.dto.VehicleDTO;
import hu.bme.aut.haulagecompany.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setName(vehicleDTO.getName());
        vehicle.setType(vehicleDTO.getType());
        vehicle.setLicensePlate(vehicleDTO.getLicensePlate());

        // You can set additional fields as needed

        return vehicleRepository.save(vehicle);
    }

    public Iterable<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public Vehicle updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle existingVehicle = getVehicleById(id);
        if (existingVehicle != null) {
            existingVehicle.setName(vehicleDTO.getName());
            existingVehicle.setType(vehicleDTO.getType());
            existingVehicle.setLicensePlate(vehicleDTO.getLicensePlate());

            // Update additional fields as needed

            return vehicleRepository.save(existingVehicle);
        }
        return null;
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }
}
