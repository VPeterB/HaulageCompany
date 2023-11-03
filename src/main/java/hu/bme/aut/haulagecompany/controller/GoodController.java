package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.Good;
import hu.bme.aut.haulagecompany.model.dto.GoodDTO;
import hu.bme.aut.haulagecompany.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goods")
public class GoodController {
    @Autowired
    private GoodService goodService;

    @PostMapping
    public ResponseEntity<Good> createGood(@RequestBody GoodDTO goodDTO) {
        Good createdGood = goodService.createGood(goodDTO);
        return new ResponseEntity<>(createdGood, HttpStatus.CREATED);
    }

    @GetMapping
    public Iterable<Good> getAllGoods() {
        return goodService.getAllGoods();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Good> getGoodById(@PathVariable Long id) {
        Good good = goodService.getGoodById(id);
        if (good != null) {
            return new ResponseEntity<>(good, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Good> updateGood(@PathVariable Long id, @RequestBody GoodDTO goodDTO) {
        Good updatedGood = goodService.updateGood(id, goodDTO);
        if (updatedGood != null) {
            return new ResponseEntity<>(updatedGood, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGood(@PathVariable Long id) {
        goodService.deleteGood(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
