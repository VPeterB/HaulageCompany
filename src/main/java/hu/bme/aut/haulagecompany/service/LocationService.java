package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Location;
import hu.bme.aut.haulagecompany.model.dto.LocationDTO;
import hu.bme.aut.haulagecompany.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public Location createLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.getName());
        location.setAddress(locationDTO.getAddress());
        return locationRepository.save(location);
    }

    public Iterable<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public Location updateLocation(Long id, LocationDTO locationDTO) {
        Location existingLocation = getLocationById(id);
        if (existingLocation != null) {
            existingLocation.setName(locationDTO.getName());
            existingLocation.setAddress(locationDTO.getAddress());
            return locationRepository.save(existingLocation);
        }
        return null;
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
