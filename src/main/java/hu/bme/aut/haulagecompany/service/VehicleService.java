package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.TransportOperation;
import hu.bme.aut.haulagecompany.model.dto.VehicleDTO;
import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.repository.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final LorrySiteService lorrySiteService;
    private final ModelMapper modelMapper;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, LorrySiteService lorrySiteService, ModelMapper modelMapper) {
        this.vehicleRepository = vehicleRepository;
        this.lorrySiteService = lorrySiteService;
        this.modelMapper = modelMapper;
    }

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        if(vehicleRepository.findByLicensePlate(vehicleDTO.getLicensePlate()).isPresent()){
            return new VehicleDTO();
        }
        Vehicle vehicle = convertToEntity(vehicleDTO);
        vehicle.setLocation((lorrySiteService.findById(vehicleDTO.getLorrySiteID())).orElse(null));
        if(vehicle.getLocation() == null){
            return null;
        }
        vehicle.setTransportOperations(new ArrayList<>());
        Vehicle createdVehicle = vehicleRepository.save(vehicle);
        lorrySiteService.addVehicle(createdVehicle);
        return convertToDTO(createdVehicle);
    }

    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = (List<Vehicle>) vehicleRepository.findAll();
        return vehicles.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public VehicleDTO getVehicleById(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.map(this::convertToDTO).orElse(null);
    }

    public VehicleDTO updateVehicle(Long id, VehicleDTO updatedVehicleDTO) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findById(id);

        if (existingVehicle.isPresent()) {
            Vehicle updatedVehicle = existingVehicle.get();
            updatedVehicle.setLicensePlate(updatedVehicleDTO.getLicensePlate());
            updatedVehicle.setSize(updatedVehicleDTO.getSize());
            updatedVehicle.setMaxWeight(updatedVehicleDTO.getMaxWeight());
            updatedVehicle.setLocation(lorrySiteService.findById(updatedVehicleDTO.getLorrySiteID()).orElse(existingVehicle.get().getLocation()));
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
        return StreamSupport.stream(vehicleRepository.findAllById(usedVehicleIDs).spliterator(), false).toList();
    }

    public void addTransportOperation(List<Vehicle> vehicles, TransportOperation createdTransportOperation) {
        for(Vehicle v : vehicles){
            Optional<Vehicle> currVehicle = vehicleRepository.findById(v.getId());
            if(currVehicle.isPresent()){
                Vehicle realV = currVehicle.get();
                List<TransportOperation> tOList = realV.getTransportOperations();
                tOList.add(createdTransportOperation);
                realV.setTransportOperations(tOList);
                vehicleRepository.save(realV);
            }
        }
    }
}
