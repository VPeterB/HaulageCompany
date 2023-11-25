package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Good;
import hu.bme.aut.haulagecompany.model.LorrySite;
import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.model.dto.LorrySiteDTO;
import hu.bme.aut.haulagecompany.model.*;
import hu.bme.aut.haulagecompany.model.dto.StackedGoodDTO;
import hu.bme.aut.haulagecompany.repository.GoodRepository;
import hu.bme.aut.haulagecompany.repository.LorrySiteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LorrySiteService {
    private final LorrySiteRepository lorrySiteRepository;
    private final GoodRepository goodRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LorrySiteService(
            LorrySiteRepository lorrySiteRepository,
            GoodRepository goodRepository,
            ModelMapper mapper) {
        this.lorrySiteRepository = lorrySiteRepository;
        this.goodRepository = goodRepository;
        this.modelMapper = mapper;
    }

    public LorrySiteDTO createLocation(LorrySiteDTO lorrySiteDTO) {
        LorrySite lorrySite = convertToEntity(lorrySiteDTO);
        lorrySite.setGoods(new ArrayList<>());
        lorrySite.setVehicles(new ArrayList<>());
        LorrySite createdLocation = lorrySiteRepository.save(lorrySite);
        return convertToDTO(createdLocation);
    }

    public List<LorrySiteDTO> getAllLocations() {
        List<LorrySite> lorrySites = (List<LorrySite>) lorrySiteRepository.findAll();
        return lorrySites.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public LorrySiteDTO getLocationById(Long id) {
        Optional<LorrySite> lorrySite = lorrySiteRepository.findById(id);
        return lorrySite.map(this::convertToDTO).orElse(null);
    }

    public LorrySiteDTO updateLocation(Long id, LorrySiteDTO updatedLocationDTO) {
        Optional<LorrySite> existingLocation = lorrySiteRepository.findById(id);

        if (existingLocation.isPresent()) {
            LorrySite updatedLocation = existingLocation.get();
            updatedLocation.setId(id);
            updatedLocation.setName(updatedLocationDTO.getName());
            updatedLocation.setAddress(updatedLocationDTO.getAddress());
            LorrySite savedLocation = lorrySiteRepository.save(updatedLocation);
            return convertToDTO(savedLocation);
        } else {
            return null;
        }
    }

    public void deleteLocation(Long id) {
        lorrySiteRepository.deleteById(id);
    }

    private LorrySiteDTO convertToDTO(LorrySite lorrySite) {
        return modelMapper.map(lorrySite, LorrySiteDTO.class);
    }

    private LorrySite convertToEntity(LorrySiteDTO lorrySiteDTO) {
        return modelMapper.map(lorrySiteDTO, LorrySite.class);
    }

    public Optional<LorrySite> findById(Long lorrySiteID) {
        return lorrySiteRepository.findById(lorrySiteID);
    }

    public void addVehicle(Vehicle createdVehicle) {
        Optional<LorrySite> edit = lorrySiteRepository.findById(createdVehicle.getLocation().getId());
        if(edit.isPresent()){
            LorrySite editable = edit.get();
            List<Vehicle> vehicleList = editable.getVehicles();
            vehicleList.add(createdVehicle);
            editable.setVehicles(vehicleList);
            lorrySiteRepository.save(editable);
        }
    }

    public LorrySiteDTO addGood(Long lorrySiteId, StackedGoodDTO good) {
        Optional<LorrySite> edit = lorrySiteRepository.findById(lorrySiteId);
        if(edit.isPresent()){
            LorrySite editable = edit.get();
            List<InventoryGood> goodList = editable.getGoods();
            goodList.add(convertToInventoryGood(lorrySiteId, good));
            List<InventoryGood> aggregatedGoodList = aggregateGoods(goodList, true);
            editable.setGoods(aggregatedGoodList);
            return convertToDTO(lorrySiteRepository.save(editable));
        }
        return null;
    }

    private InventoryGood convertToInventoryGood(Long lorrySiteId, StackedGoodDTO good) { //TODO create
        InventoryGood ig = new InventoryGood();
        LorrySite ls = lorrySiteRepository.findById(lorrySiteId).orElse(null);
        ig.setLorrySite(ls);
        ig.setQuantity(good.getQuantity());
        Good g = goodRepository.findById(good.getGoodId()).orElse(null);
        ig.setGood(g);
        return ig;
    }

    public void removeGoods(LorrySite lorrySite, List<InventoryGood> goods) {
        var lS = lorrySiteRepository.findById(lorrySite.getId());
        if(lS.isPresent()){
            var realLS = lS.get();
            var lSGoods = new ArrayList<InventoryGood>();
            lSGoods.addAll(realLS.getGoods());
            lSGoods.addAll(goods);
            var newGoods = removeGoods(lSGoods);
            realLS.setGoods(newGoods);
            lorrySiteRepository.save(realLS);
        }
    }

    private List<InventoryGood> removeGoods(ArrayList<InventoryGood> goods) {
        return aggregateGoods(goods, false);
    }

    public List<InventoryGood> aggregateGoods(List<InventoryGood> goods, boolean add){
        Map<String, InventoryGood> aggregatedGoods = new HashMap<>();
        for (InventoryGood good : goods) {
            String key = getKey(good);
            if (aggregatedGoods.containsKey(key)) {
                InventoryGood existingGood = aggregatedGoods.get(key);
                if(add){
                    existingGood.setQuantity(existingGood.getQuantity() + good.getQuantity());
                }else{
                    existingGood.setQuantity(existingGood.getQuantity() - good.getQuantity());
                }
            } else {
                aggregatedGoods.put(key, good);
            }
        }
        return new ArrayList<>(aggregatedGoods.values());
    }

    private String getKey(InventoryGood good) { //Good unique key
        return good.getGood().getName() + "_" + good.getGood().getSize() + "_" + good.getGood().getWeight();
    }
}
