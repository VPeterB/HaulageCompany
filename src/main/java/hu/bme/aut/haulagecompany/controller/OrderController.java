package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.dto.OrderDTO;
import hu.bme.aut.haulagecompany.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createPurchase(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdPurchase = orderService.createPurchase(orderDTO);
        return new ResponseEntity<>(createdPurchase, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllPurchases() {
        return new ResponseEntity<>(orderService.getAllPurchases(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getPurchaseById(@PathVariable Long id) {
        OrderDTO purchase = orderService.getPurchaseById(id);
        if (purchase != null) {
            return new ResponseEntity<>(purchase, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updatePurchase(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedPurchase = orderService.updatePurchase(id, orderDTO);
        if (updatedPurchase != null) {
            return new ResponseEntity<>(updatedPurchase, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        orderService.deletePurchase(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
