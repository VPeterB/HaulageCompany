package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.dto.LorrySiteDTO;
import hu.bme.aut.haulagecompany.service.LorrySiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LorrySiteController {
    @Autowired
    private LorrySiteService lorrySiteService;

    @PostMapping
    public ResponseEntity<LorrySiteDTO> createLocation(@RequestBody LorrySiteDTO locationDTO) {
        LorrySiteDTO createdLocation = lorrySiteService.createLocation(locationDTO);
        return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LorrySiteDTO>> getAllLocations() {
        return new ResponseEntity<>(lorrySiteService.getAllLocations(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LorrySiteDTO> getLocationById(@PathVariable Long id) {
        LorrySiteDTO location = lorrySiteService.getLocationById(id);
        if (location != null) {
            return new ResponseEntity<>(location, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LorrySiteDTO> updateLocation(@PathVariable Long id, @RequestBody LorrySiteDTO locationDTO) {
        LorrySiteDTO updatedLocation = lorrySiteService.updateLocation(id, locationDTO);
        if (updatedLocation != null) {
            return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        lorrySiteService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //TODO addGoodsToStorage
}
