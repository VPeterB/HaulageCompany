package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Good;
import hu.bme.aut.haulagecompany.model.LorrySite;
import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.model.dto.GetStackedGoodDTO;
import hu.bme.aut.haulagecompany.model.dto.GoodDTO;
import hu.bme.aut.haulagecompany.model.dto.LorrySiteDTO;
import hu.bme.aut.haulagecompany.model.*;
import hu.bme.aut.haulagecompany.model.dto.StackedGoodDTO;
import hu.bme.aut.haulagecompany.repository.InventoryGoodRepository;
import hu.bme.aut.haulagecompany.repository.LorrySiteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LorrySiteService {
    private final LorrySiteRepository lorrySiteRepository;
    private final InventoryGoodRepository inventoryGoodRepository;
    private final GoodService goodService;
    private final ModelMapper modelMapper;

    @Autowired
    public LorrySiteService(
            LorrySiteRepository lorrySiteRepository,
            InventoryGoodRepository inventoryGoodRepository,
            GoodService goodService,
            ModelMapper mapper) {
        this.lorrySiteRepository = lorrySiteRepository;
        this.inventoryGoodRepository = inventoryGoodRepository;
        this.goodService = goodService;
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
        LorrySiteDTO lsDTO = modelMapper.map(lorrySite, LorrySiteDTO.class);
        lsDTO.setGoodDTOs(convertToGetStackedGoodDTO(lorrySite.getGoods()));
        return lsDTO;
    }

    private List<GetStackedGoodDTO> convertToGetStackedGoodDTO(List<InventoryGood> goods) {
        List<GetStackedGoodDTO> sgList = new ArrayList<>();
        for(InventoryGood g : goods){
            GetStackedGoodDTO sgDTO = new GetStackedGoodDTO();
            GoodDTO gDTO = goodService.convertToDTO(g.getGood());
            if(gDTO == null){
                continue;
            }
            sgDTO.setGoodDTO(gDTO);
            sgDTO.setQuantity(g.getQuantity());
            sgList.add(sgDTO);
        }
        return sgList;
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
            InventoryGood newG = convertToInventoryGood(lorrySiteId, good);
            goodList.add(newG);
            List<InventoryGood> aggregatedGoodList = aggregateGoods(goodList, true);
            List<InventoryGood> savedAggregatedGoodList = saveGoods(aggregatedGoodList);
            editable.setGoods(savedAggregatedGoodList);
            return convertToDTO(lorrySiteRepository.save(editable));
        }
        return null;
    }

    private List<InventoryGood> saveGoods(List<InventoryGood> aggregatedGoodList) {
        List<InventoryGood> res = new ArrayList<>();
        for(InventoryGood g : aggregatedGoodList){
            res.add(inventoryGoodRepository.save(g));
        }
        return res;
    }

    private InventoryGood convertToInventoryGood(Long lorrySiteId, StackedGoodDTO good) {
        InventoryGood ig = new InventoryGood();
        LorrySite ls = lorrySiteRepository.findById(lorrySiteId).orElse(null);
        ig.setLorrySite(ls);
        ig.setQuantity(good.getQuantity());
        Good g = goodService.findById(good.getGoodId()).orElse(null);
        ig.setGood(g);
        return ig;
    }

    public void removeGoods(LorrySite lorrySite, List<OrderedGood> goods) {
        var lS = lorrySiteRepository.findById(lorrySite.getId());
        if(lS.isPresent()){
            var realLS = lS.get();
            var lSGoods = new ArrayList<InventoryGood>();
            lSGoods.addAll(realLS.getGoods());
            lSGoods.addAll(ogConvertToInventoryGood(realLS.getId(), goods));
            var newGoods = removeGoods(lSGoods);
            realLS.setGoods(newGoods);
            lorrySiteRepository.save(realLS);
        }
    }

    private ArrayList<InventoryGood> ogConvertToInventoryGood(Long id, List<OrderedGood> goods) {
        ArrayList<InventoryGood> igl = new ArrayList<>();
        for(OrderedGood og : goods){
            InventoryGood ig = new InventoryGood();
            ig.setGood(og.getGood());
            ig.setLorrySite(lorrySiteRepository.findById(id).orElse(null));
            ig.setQuantity(og.getQuantity());
            igl.add(ig);
        }
        return igl;
    }

    private List<InventoryGood> removeGoods(ArrayList<InventoryGood> goods) {
        List<InventoryGood> aggregatedGoodList = aggregateGoods(goods, false);
        return saveGoods(aggregatedGoodList);
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
        if(good.getGood() != null){
            return good.getGood().getName() + "_" + good.getGood().getSize() + "_" + good.getGood().getWeight();
        }
        return "";
    }
}
