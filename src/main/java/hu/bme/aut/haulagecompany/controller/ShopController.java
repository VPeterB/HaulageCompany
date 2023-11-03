package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.Shop;
import hu.bme.aut.haulagecompany.model.dto.ShopDTO;
import hu.bme.aut.haulagecompany.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shops")
public class ShopController {
    @Autowired
    private ShopService shopService;

    @PostMapping
    public ResponseEntity<Shop> createShop(@RequestBody ShopDTO shopDTO) {
        Shop createdShop = shopService.createShop(shopDTO);
        return new ResponseEntity<>(createdShop, HttpStatus.CREATED);
    }

    @GetMapping
    public Iterable<Shop> getAllShops() {
        return shopService.getAllShops();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShopById(@PathVariable Long id) {
        Shop shop = shopService.getShopById(id);
        if (shop != null) {
            return new ResponseEntity<>(shop, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long id, @RequestBody ShopDTO shopDTO) {
        Shop updatedShop = shopService.updateShop(id, shopDTO);
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
