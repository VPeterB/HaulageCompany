package hu.bme.aut.haulagecompany.controller;

import hu.bme.aut.haulagecompany.model.dto.GoodDTO;
import hu.bme.aut.haulagecompany.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@Validated
@RequestMapping("/api/goods")
public class GoodController {
    @Autowired
    private GoodService goodService;

    @PostMapping("/{lorrySiteId}")
    public ResponseEntity<GoodDTO> createGood(@PathVariable Long lorrySiteId, @Validated @RequestBody GoodDTO goodDTO) {
        GoodDTO createdGood = goodService.createGood(lorrySiteId, goodDTO);
        return new ResponseEntity<>(createdGood, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GoodDTO>> getAllGoods() {
        return new ResponseEntity<>(goodService.getAllGoods(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodDTO> getGoodById(@PathVariable Long id) {
        GoodDTO good = goodService.getGoodById(id);
        if (Objects.nonNull(good)) {
            return new ResponseEntity<>(good, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoodDTO> updateGood(@PathVariable Long id, @RequestBody GoodDTO goodDTO) {
        GoodDTO updatedGood = goodService.updateGood(id, goodDTO);
        if (Objects.nonNull(updatedGood)) {
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
