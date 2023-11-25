package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Good;
import hu.bme.aut.haulagecompany.model.dto.GoodDTO;
import hu.bme.aut.haulagecompany.repository.GoodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GoodService {
    private final GoodRepository goodRepository;
    private final ModelMapper modelMapper;
    private final LorrySiteService lorrySiteService;

    @Autowired
    public GoodService(GoodRepository goodRepository, ModelMapper modelMapper, LorrySiteService lorrySiteService) {
        this.goodRepository = goodRepository;
        this.modelMapper = modelMapper;
        this.lorrySiteService = lorrySiteService;
    }

    public GoodDTO createGood(GoodDTO goodDTO) {
        Good good = convertToEntity(goodDTO);
        Good createdGood = goodRepository.save(good);
        return convertToDTO(createdGood);
    }

    public List<GoodDTO> getAllGoods() {
        return StreamSupport.stream(goodRepository.findAll().spliterator(), false)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GoodDTO getGoodById(Long id) {
        Optional<Good> good = goodRepository.findById(id);
        return good.map(this::convertToDTO).orElse(null);
    }

    public GoodDTO updateGood(Long id, GoodDTO updatedGoodDTO) {
        Optional<Good> existingGood = goodRepository.findById(id);

        if (existingGood.isPresent()) {
            Good updatedGood = convertToEntity(updatedGoodDTO);
            updatedGood.setId(id);

            Good savedGood = goodRepository.save(updatedGood);
            return convertToDTO(savedGood);
        } else {
            return null;
        }
    }

    public void deleteGood(Long id) {
        goodRepository.deleteById(id);
    }

    private GoodDTO convertToDTO(Good good) {
        return modelMapper.map(good, GoodDTO.class);
    }

    private Good convertToEntity(GoodDTO goodDTO) {
        return modelMapper.map(goodDTO, Good.class);
    }

    public List<Good> getGoodsByIds(List<Long> goodIDs) {
        return StreamSupport.stream(goodRepository.findAllById(goodIDs).spliterator(), false).toList();
    }
}
