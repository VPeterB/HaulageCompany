package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.LorrySite;
import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.model.dto.LorrySiteDTO;
import hu.bme.aut.haulagecompany.repository.LorrySiteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LorrySiteService {
    private final LorrySiteRepository lorrySiteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LorrySiteService(
            LorrySiteRepository lorrySiteRepository) {
        this.lorrySiteRepository = lorrySiteRepository;
        this.modelMapper = new ModelMapper();
    }

    public LorrySiteDTO createLocation(LorrySiteDTO lorrySiteDTO) {
        LorrySite lorrySite = convertToEntity(lorrySiteDTO);
        lorrySite.setGoods(new ArrayList<>());
        lorrySite.setVehicles(new ArrayList<>());
        LorrySite createdLocation = lorrySiteRepository.save(lorrySite);
        return convertToDTO(createdLocation);
    }

    public List<LorrySiteDTO> getAllLocations() {
        List<LorrySite> lorrySites = (List<LorrySite>) lorrySiteRepository.findAll();
        return lorrySites.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public LorrySiteDTO getLocationById(Long id) {
        Optional<LorrySite> lorrySite = lorrySiteRepository.findById(id);
        return lorrySite.map(this::convertToDTO).orElse(null);
    }

    public LorrySiteDTO updateLocation(Long id, LorrySiteDTO updatedLocationDTO) {
        Optional<LorrySite> existingLocation = lorrySiteRepository.findById(id);

        if (existingLocation.isPresent()) {
            LorrySite updatedLocation = convertToEntity(updatedLocationDTO);
            updatedLocation.setId(id);

            LorrySite savedLocation = lorrySiteRepository.save(updatedLocation);
            return convertToDTO(savedLocation);
        } else {
            return null;
        }
    }

    public void deleteLocation(Long id) {
        lorrySiteRepository.deleteById(id);
    }

    private LorrySiteDTO convertToDTO(LorrySite lorrySite) {
        return modelMapper.map(lorrySite, LorrySiteDTO.class);
    }

    private LorrySite convertToEntity(LorrySiteDTO lorrySiteDTO) {
        return modelMapper.map(lorrySiteDTO, LorrySite.class);
    }

    public Optional<LorrySite> findById(Long lorrySiteID) {
        return lorrySiteRepository.findById(lorrySiteID);
    }

    public void addVehicle(Vehicle createdVehicle) {
        Optional<LorrySite> edit = lorrySiteRepository.findById(createdVehicle.getLocation().getId());
        if(edit.isPresent()){
            LorrySite editable = edit.get();
            List<Vehicle> vehicleList = editable.getVehicles();
            vehicleList.add(createdVehicle);
            editable.setVehicles(vehicleList);
            lorrySiteRepository.save(edit.get());
        }
    }
}
