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
    public ResponseEntity<TransportOperationDTO> createTransportOperation(@RequestBody TransportOperationDTO transportOperationDTO) {
        TransportOperationDTO createdTransportOperation = transportOperationService.createTransportOperation(transportOperationDTO);
        return new ResponseEntity<>(createdTransportOperation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransportOperationDTO>> getAllTransportOperations() {
        return new ResponseEntity<>(transportOperationService.getAllTransportOperations(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportOperationDTO> getTransportOperationById(@PathVariable Long id) {
        TransportOperationDTO transportOperation = transportOperationService.getTransportOperationById(id);
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
