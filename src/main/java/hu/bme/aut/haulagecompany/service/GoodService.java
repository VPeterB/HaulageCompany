package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.*;
import hu.bme.aut.haulagecompany.model.dto.GoodDTO;
import hu.bme.aut.haulagecompany.repository.GoodRepository;
import hu.bme.aut.haulagecompany.repository.StackedGoodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class GoodService {
    private final GoodRepository goodRepository;
    private final ModelMapper modelMapper;
    private final StackedGoodRepository stackedGoodRepository;

    @Autowired
    public GoodService(GoodRepository goodRepository, ModelMapper modelMapper,
                       StackedGoodRepository stackedGoodRepository) {
        this.goodRepository = goodRepository;
        this.modelMapper = modelMapper;
        this.stackedGoodRepository = stackedGoodRepository;
    }

    public GoodDTO createGood(GoodDTO goodDTO) {
        Good good = convertToEntity(goodDTO);
        Good createdGood = goodRepository.save(good);
        return convertToDTO(createdGood);
    }

    public List<GoodDTO> getAllGoods() {
        return StreamSupport.stream(goodRepository.findAll().spliterator(), false)
                .map(this::convertToDTO)
                .toList();
    }

    public GoodDTO getGoodDTOById(Long id) {
        Optional<Good> good = goodRepository.findById(id);
        return good.map(this::convertToDTO).orElse(null);
    }

    public GoodDTO updateGood(Long id, GoodDTO updatedGoodDTO) {
        Optional<Good> existingGood = goodRepository.findById(id);

        if (existingGood.isPresent()) {
            List<StackedGood> sg = stackedGoodRepository.findAllByGoodId(id);
            if(sg.isEmpty()){
                Good updatedGood = convertToEntity(updatedGoodDTO);
                updatedGood.setId(id);
                Good savedGood = goodRepository.save(updatedGood);
                return convertToDTO(savedGood);
            }
            Good updatedGood = existingGood.get();
            updatedGood.setName(updatedGoodDTO.getName());
            updatedGood.setDescription(updatedGoodDTO.getDescription());
            Good savedGood = goodRepository.save(updatedGood);
            return convertToDTO(savedGood);
        } else {
            return null;
        }
    }

    public void deleteGood(Long id) {
        Optional<Good> g = goodRepository.findById(id);
        if(g.isPresent()){
            List<StackedGood> sg = stackedGoodRepository.findAllByGoodId(id);
            for(StackedGood stackedGood : sg){
                stackedGood.setGood(null);
                stackedGoodRepository.save(stackedGood);
            }
        }
        goodRepository.deleteById(id);
    }

    public GoodDTO convertToDTO(Good good) {
        if(good != null){
            return modelMapper.map(good, GoodDTO.class);
        }
        return null;
    }

    private Good convertToEntity(GoodDTO goodDTO) {
        return modelMapper.map(goodDTO, Good.class);
    }


    public Optional<Good> findById(Long goodId) {
        return goodRepository.findById(goodId);
    }
}
