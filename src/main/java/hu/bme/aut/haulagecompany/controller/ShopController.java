package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.dto.ShopDTO;
import hu.bme.aut.haulagecompany.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
public class ShopController {
    @Autowired
    private ShopService shopService;

    @PostMapping
    public ResponseEntity<ShopDTO> createShop(@RequestBody ShopDTO shopDTO) {
        ShopDTO createdShop = shopService.createShop(shopDTO);
        return new ResponseEntity<>(createdShop, HttpStatus.CREATED);
    }

    @GetMapping
    public  ResponseEntity<List<ShopDTO>> getAllShops() {
        return new ResponseEntity<>(shopService.getAllShops(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopDTO> getShopById(@PathVariable Long id) {
        ShopDTO shop = shopService.getShopDTOById(id);
        if (shop != null) {
            return new ResponseEntity<>(shop, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopDTO> updateShop(@PathVariable Long id, @RequestBody ShopDTO shopDTO) {
        ShopDTO updatedShop = shopService.updateShop(id, shopDTO);
        if (updatedShop != null) {
            return new ResponseEntity<>(updatedShop, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        shopService.deleteShop(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
