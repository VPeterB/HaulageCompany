package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Location;
import hu.bme.aut.haulagecompany.model.LorrySite;
import hu.bme.aut.haulagecompany.model.dto.LorrySiteDTO;
import hu.bme.aut.haulagecompany.repository.LorrySiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LorrySiteService {
    @Autowired
    private LorrySiteRepository lorrySiteRepository;

    public LorrySiteDTO createLocation(LorrySiteDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.getName());
        location.setAddress(locationDTO.getAddress());
        return lorrySiteRepository.save(location);
    }

    public List<LorrySiteDTO> getAllLocations() {
        return lorrySiteRepository.findAll();
    }

    public LorrySiteDTO getLocationById(Long id) {
        return lorrySiteRepository.findById(id).orElse(null);
    }

    public LorrySiteDTO updateLocation(Long id, LorrySiteDTO locationDTO) {
        LorrySite existingLocation = getLocationById(id);
        if (existingLocation != null) {
            existingLocation.setName(locationDTO.getName());
            existingLocation.setAddress(locationDTO.getAddress());
            return lorrySiteRepository.save(existingLocation);
        }
        return null;
    }

    public void deleteLocation(Long id) {
        lorrySiteRepository.deleteById(id);
    }
}
