package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.*;
import hu.bme.aut.haulagecompany.model.dto.TransportOperationDTO;
import hu.bme.aut.haulagecompany.repository.TransportOperationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.ToDoubleFunction;

@Service
public class TransportOperationService {
    private final TransportOperationRepository transportOperationRepository;
    private final ModelMapper modelMapper;
    private final VehicleService vehicleService;
    private final OrderService orderService;
    private final LorrySiteService lorrySiteService;

    @Autowired
    public TransportOperationService(
            TransportOperationRepository transportOperationRepository,
            VehicleService vehicleService,
            OrderService orderService,
            ModelMapper modelMapper,
            LorrySiteService lorrySiteService) {
        this.transportOperationRepository = transportOperationRepository;
        this.modelMapper = modelMapper;
        this.vehicleService = vehicleService;
        this.orderService = orderService;
        this.lorrySiteService = lorrySiteService;
    }

    public TransportOperationDTO createTransportOperation(TransportOperationDTO transportOperationDTO) {
        TransportOperation transportOperation = convertToEntity(transportOperationDTO);
        if(transportOperationRepository.findByOrderId(transportOperationDTO.getOrderID()).isPresent()){
            return null;
        }
        if(!checkVehicleAvailable(transportOperationDTO.getDate(), transportOperationDTO.getUsedVehicleIDs()) && !checkGoodsAvailable(transportOperationDTO) && checkVehicleSizesAreGood(transportOperationDTO)){
            return null;
        }
        transportOperation.setUsedVehicles(vehicleService.getVehiclesByIds(transportOperationDTO.getUsedVehicleIDs()));
        transportOperation.setOrder(orderService.getOrderById(transportOperationDTO.getOrderID()));
        TransportOperation createdTransportOperation = transportOperationRepository.save(transportOperation);


        var vehicles = vehicleService.getVehiclesByIds(transportOperationDTO.getUsedVehicleIDs());
        lorrySiteService.removeGoods(vehicles.get(0).getLocation(), orderService.getOrderById(transportOperationDTO.getOrderID()).getGoods());
        return convertToDTO(createdTransportOperation);
    }

    private boolean checkVehicleSizesAreGood(TransportOperationDTO transportOperationDTO) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByIds(transportOperationDTO.getUsedVehicleIDs());
        double summedVehicleSize = sumAttribute(vehicles, Vehicle::getSize);
        double summedVehicleMaxWeight = sumAttribute(vehicles, Vehicle::getMaxWeight);

        Order order = orderService.getOrderById(transportOperationDTO.getOrderID());
        List<Good> orderedGoods = order.getGoods();
        double summedOrderedGoodsSize = sumAttribute(orderedGoods, Good::getSize);
        double summedOrderedGoodsWeight = sumAttribute(orderedGoods, Good::getWeight);

        return (summedOrderedGoodsSize <= summedVehicleSize) && (summedOrderedGoodsWeight <= summedVehicleMaxWeight);
    }

    private <T> double sumAttribute(List<T> items, ToDoubleFunction<T> attributeExtractor) {
        return items.stream()
                .mapToDouble(attributeExtractor)
                .sum();
    }

    private boolean checkGoodsAvailable(TransportOperationDTO transportOperationDTO) {
        var order = orderService.getOrderById(transportOperationDTO.getOrderID());
        var vehicles = vehicleService.getVehiclesByIds(transportOperationDTO.getUsedVehicleIDs());
        var lorrySites = new ArrayList<LorrySite>();
        vehicles.forEach(v -> lorrySites.add(v.getLocation()));
        var lorrySiteGoods = new ArrayList<Good>();
        lorrySites.forEach(l -> lorrySiteGoods.addAll(l.getGoods()));
        var summedGoods = lorrySiteService.aggregateGoods(lorrySiteGoods, true);
        var orderedGoods = order.getGoods();
        AtomicBoolean res = new AtomicBoolean(true);
        orderedGoods.forEach(og -> summedGoods.forEach(g -> {
            if(Objects.equals(og.getName(), g.getName()) && Objects.equals(og.getSize(), g.getSize()) && Objects.equals(og.getWeight(), g.getWeight())
                    && og.getQuantity() > g.getQuantity()){
                res.set(false);
            }
        }));
        return res.get();
    }

    private boolean checkVehicleAvailable(Timestamp date, List<Long> usedVehicleIDs) {
        var usedVehicles = vehicleService.getVehiclesByIds(usedVehicleIDs);
        for(Vehicle v : usedVehicles){
            var vTO = v.getTransportOperations();
            for(TransportOperation to: vTO){
                if(to.getDate().equals(date)){
                    return false;
                }
            }
        }
        return true;
    }

    public List<TransportOperationDTO> getAllTransportOperations() {
        List<TransportOperation> transportOperations = (List<TransportOperation>) transportOperationRepository.findAll();
        return transportOperations.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public TransportOperationDTO getTransportOperationDTOById(Long id) {
        Optional<TransportOperation> transportOperation = transportOperationRepository.findById(id);
        return transportOperation.map(this::convertToDTO).orElse(null);
    }

    public TransportOperationDTO updateTransportOperation(Long id, TransportOperationDTO updatedTransportOperationDTO) {
        Optional<TransportOperation> existingTransportOperation = transportOperationRepository.findById(id);

        if (existingTransportOperation.isPresent()) {
            TransportOperation updatedTransportOperation = convertToEntity(updatedTransportOperationDTO);
            updatedTransportOperation.setId(id);
            updatedTransportOperation.setUsedVehicles(vehicleService.getVehiclesByIds(updatedTransportOperationDTO.getUsedVehicleIDs()));
            updatedTransportOperation.setOrder(orderService.getOrderById(updatedTransportOperationDTO.getOrderID()));

            TransportOperation savedTransportOperation = transportOperationRepository.save(updatedTransportOperation);
            return convertToDTO(savedTransportOperation);
        } else {
            return null;
        }
    }

    public void deleteTransportOperation(Long id) {
        var tO = transportOperationRepository.findById(id);
        if(tO.isPresent()){
            var realTO = tO.get();
            var lSId = realTO.getUsedVehicles().get(1).getLocation().getId();
            assert realTO.getOrder() != null;
            for(Good g : realTO.getOrder().getGoods()){
                lorrySiteService.addGood(lSId, g);
            }
        }
        transportOperationRepository.deleteById(id);
    }

    private TransportOperationDTO convertToDTO(TransportOperation transportOperation) {
        return modelMapper.map(transportOperation, TransportOperationDTO.class);
    }

    private TransportOperation convertToEntity(TransportOperationDTO transportOperationDTO) {
        return modelMapper.map(transportOperationDTO, TransportOperation.class);
    }
}
