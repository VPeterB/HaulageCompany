package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Good;
import hu.bme.aut.haulagecompany.model.LorrySite;
import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.model.dto.GoodDTO;
import hu.bme.aut.haulagecompany.model.dto.LorrySiteDTO;
import hu.bme.aut.haulagecompany.model.dto.VehicleDTO;
import hu.bme.aut.haulagecompany.repository.GoodRepository;
import hu.bme.aut.haulagecompany.repository.LorrySiteRepository;
import hu.bme.aut.haulagecompany.repository.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class LorrySiteService {
    private final LorrySiteRepository lorrySiteRepository;
    private final GoodRepository goodRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LorrySiteService(
            LorrySiteRepository lorrySiteRepository,
            ModelMapper mapper,
            GoodRepository goodRepository,
            VehicleRepository vehicleRepository) {
        this.lorrySiteRepository = lorrySiteRepository;
        this.modelMapper = mapper;
        this.goodRepository = goodRepository;
        this.vehicleRepository = vehicleRepository;
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
            List<Long> goodIDs = new ArrayList<>();
            if(updatedLocationDTO.getGoodDTOs() != null) {
                for (GoodDTO goodDTO : updatedLocationDTO.getGoodDTOs()) {
                    goodIDs.add(goodDTO.getId());
                }
            }
            List<Good> goods = StreamSupport.stream(goodRepository.findAllById(goodIDs).spliterator(), false).toList();
            updatedLocation.setGoods(goods);
            List<Long> vehicleIDs = new ArrayList<>();
            if(updatedLocationDTO.getVehicleDTOs() != null) {
                for (VehicleDTO vehicleDTO : updatedLocationDTO.getVehicleDTOs()) {
                    goodIDs.add(vehicleDTO.getId());
                }
            }
            List<Vehicle> vehicles = StreamSupport.stream(vehicleRepository.findAllById(vehicleIDs).spliterator(), false).toList();
            updatedLocation.setVehicles(vehicles);
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

    public void addGood(Long lorrySiteId, Good createdGood) {
        Optional<LorrySite> edit = lorrySiteRepository.findById(lorrySiteId);
        if(edit.isPresent()){
            LorrySite editable = edit.get();
            List<Good> goodList = editable.getGoods();
            goodList.add(createdGood);
            List<Good> aggregatedGoodList = aggregateGoods(goodList, true);
            editable.setGoods(aggregatedGoodList);
            lorrySiteRepository.save(editable);
        }
    }

    public void removeGoods(LorrySite lorrySite, List<Good> goods) {
        var lS = lorrySiteRepository.findById(lorrySite.getId());
        if(lS.isPresent()){
            var realLS = lS.get();
            var lSGoods = new ArrayList<Good>();
            lSGoods.addAll(realLS.getGoods());
            lSGoods.addAll(goods);
            var newGoods = removeGoods(lSGoods);
            realLS.setGoods(newGoods);
            lorrySiteRepository.save(realLS);
        }
    }

    private List<Good> removeGoods(ArrayList<Good> goods) {
        return aggregateGoods(goods, false);
    }

    public List<Good> aggregateGoods(List<Good> goods, boolean add){
        Map<String, Good> aggregatedGoods = new HashMap<>();
        for (Good good : goods) {
            String key = getKey(good);
            if (aggregatedGoods.containsKey(key)) {
                Good existingGood = aggregatedGoods.get(key);
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

    private String getKey(Good good) { //Good unique key
        return good.getName() + "_" + good.getSize() + "_" + good.getWeight();
    }
}
