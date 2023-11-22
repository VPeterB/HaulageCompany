package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Good;
import hu.bme.aut.haulagecompany.model.LorrySite;
import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.model.dto.LorrySiteDTO;
import hu.bme.aut.haulagecompany.repository.LorrySiteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LorrySiteService {
    private final LorrySiteRepository lorrySiteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LorrySiteService(
            LorrySiteRepository lorrySiteRepository,
            ModelMapper mapper) {
        this.lorrySiteRepository = lorrySiteRepository;
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
                .collect(Collectors.toList());
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

    public void addGood(Long lorrySiteId, Good createdGood) {
        Optional<LorrySite> edit = lorrySiteRepository.findById(lorrySiteId);
        if(edit.isPresent()){
            LorrySite editable = edit.get();
            List<Good> goodList = editable.getGoods();
            goodList.add(createdGood);
            editable.setGoods(goodList);
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
        Map<String, Good> aggregatedGoods = new HashMap<>();
        for (Good good : goods) {
            String key = getKey(good);
            if (aggregatedGoods.containsKey(key)) {
                Good existingGood = aggregatedGoods.get(key);
                existingGood.setQuantity(existingGood.getQuantity() - good.getQuantity());
            } else {
                aggregatedGoods.put(key, good);
            }
        }
        return new ArrayList<>(aggregatedGoods.values());
    }

    private String getKey(Good good) {
        return good.getName() + "_" + good.getSize() + "_" + good.getWeight();
    }
}
