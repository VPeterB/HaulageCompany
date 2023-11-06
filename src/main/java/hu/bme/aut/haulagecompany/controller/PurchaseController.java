package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.Purchase;
import hu.bme.aut.haulagecompany.model.dto.OrderDTO;
import hu.bme.aut.haulagecompany.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody OrderDTO orderDTO) {
        Purchase createdPurchase = purchaseService.createPurchase(orderDTO);
        return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
    }

    @GetMapping
    public Iterable<Purchase> getAllPurchases() {
        return purchaseService.getAllPurchases();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        Purchase purchase = purchaseService.getPurchaseById(id);
        if (purchase != null) {
            return new ResponseEntity<>(purchase, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Purchase> updatePurchase(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        Purchase updatedPurchase = purchaseService.updatePurchase(id, orderDTO);
        if (updatedPurchase != null) {
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
