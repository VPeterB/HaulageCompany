package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.model.dto.VehicleDTO;
import hu.bme.aut.haulagecompany.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setName(vehicleDTO.getName());
        vehicle.setType(vehicleDTO.getType());
        vehicle.setLicensePlate(vehicleDTO.getLicensePlate());

        // You can set additional fields as needed

        return vehicleRepository.save(vehicle);
    }

    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public VehicleDTO getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
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
