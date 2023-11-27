package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.dto.TransportOperationDTO;
import hu.bme.aut.haulagecompany.service.TransportOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport-operations")
public class TransportOperationController {
    @Autowired
    private TransportOperationService transportOperationService;

    @PostMapping
    public ResponseEntity<?> createTransportOperation(@RequestBody TransportOperationDTO transportOperationDTO) {
        TransportOperationDTO createdTransportOperation = transportOperationService.createTransportOperation(transportOperationDTO);
        if(createdTransportOperation == null){
            return new ResponseEntity<>("Vehicle(s) not available!", HttpStatus.CONFLICT);
        }
        if(createdTransportOperation.getId() == null){
            return new ResponseEntity<>("Good(s) not available!", HttpStatus.CONFLICT);
        }
        if(createdTransportOperation.getId() == -1L){
            return new ResponseEntity<>("Vehicle(s) size not enough!", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(createdTransportOperation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransportOperationDTO>> getAllTransportOperations() {
        return new ResponseEntity<>(transportOperationService.getAllTransportOperations(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportOperationDTO> getTransportOperationById(@PathVariable Long id) {
        TransportOperationDTO transportOperation = transportOperationService.getTransportOperationDTOById(id);
        if (transportOperation != null) {
            return new ResponseEntity<>(transportOperation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportOperationDTO> updateTransportOperation(@PathVariable Long id, @RequestBody TransportOperationDTO transportOperationDTO) {
        TransportOperationDTO updatedTransportOperation = transportOperationService.updateTransportOperation(id, transportOperationDTO);
        if (updatedTransportOperation != null) {
            return new ResponseEntity<>(updatedTransportOperation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportOperation(@PathVariable Long id) {
        transportOperationService.deleteTransportOperation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
