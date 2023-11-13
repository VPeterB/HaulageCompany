package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Good;
import hu.bme.aut.haulagecompany.model.dto.GoodDTO;
import hu.bme.aut.haulagecompany.repository.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodService {
    @Autowired
    private GoodRepository<Good> goodRepository;

    public GoodDTO createGood(GoodDTO goodDTO) {

        Good good = new Good();
        good.setName(goodDTO.getName());
        good.setDescription(goodDTO.getDescription());
        good.setPrice(goodDTO.getPrice());
        return goodRepository.save(good);
    }

    public List<GoodDTO> getAllGoods() {
        return goodRepository.findAll();
    }

    public GoodDTO getGoodById(Long id) {
        return goodRepository.findById(id).orElse(null);
    }

    public GoodDTO updateGood(Long id, GoodDTO goodDTO) {
        Good existingGood = getGoodById(id);
        if (existingGood != null) {
            existingGood.setName(goodDTO.getName());
            existingGood.setDescription(goodDTO.getDescription());
            existingGood.setPrice(goodDTO.getPrice());
            return goodRepository.save(existingGood);
        }
        return null;
    }

    public void deleteGood(Long id) {
        goodRepository.deleteById(id);
    }
}
