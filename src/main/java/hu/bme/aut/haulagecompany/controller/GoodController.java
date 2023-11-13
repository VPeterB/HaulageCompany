package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.dto.GoodDTO;
import hu.bme.aut.haulagecompany.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodController {
    @Autowired
    private GoodService goodService;

    @PostMapping
    public ResponseEntity<GoodDTO> createGood(@RequestBody GoodDTO goodDTO) {
        GoodDTO createdGood = goodService.createGood(goodDTO);
        return new ResponseEntity<>(createdGood, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GoodDTO>> getAllGoods() {
        return new ResponseEntity<>(goodService.getAllGoods(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodDTO> getGoodById(@PathVariable Long id) {
        GoodDTO good = goodService.getGoodById(id);
        if (good != null) {
            return new ResponseEntity<>(good, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoodDTO> updateGood(@PathVariable Long id, @RequestBody GoodDTO goodDTO) {
        GoodDTO updatedGood = goodService.updateGood(id, goodDTO);
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
