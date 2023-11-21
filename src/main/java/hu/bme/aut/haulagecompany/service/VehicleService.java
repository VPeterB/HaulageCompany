package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.dto.VehicleDTO;
import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.repository.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, ModelMapper modelMapper) {
        this.vehicleRepository = vehicleRepository;
        this.modelMapper = modelMapper;
    }

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle = convertToEntity(vehicleDTO);
        Vehicle createdVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(createdVehicle);
    }

    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = (List<Vehicle>) vehicleRepository.findAll();
        return vehicles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.map(this::convertToDTO).orElse(null);
    }

    public VehicleDTO updateVehicle(Long id, VehicleDTO updatedVehicleDTO) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findById(id);

        if (existingVehicle.isPresent()) {
            Vehicle updatedVehicle = convertToEntity(updatedVehicleDTO);
            updatedVehicle.setId(id);

            Vehicle savedVehicle = vehicleRepository.save(updatedVehicle);
            return convertToDTO(savedVehicle);
        } else {
            return null;
        }
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    private Vehicle convertToEntity(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, Vehicle.class);
    }

    public List<Vehicle> getVehiclesByIds(List<Long> usedVehicleIDs) {
        return (List<Vehicle>) vehicleRepository.findAllById(usedVehicleIDs);
    }
}
