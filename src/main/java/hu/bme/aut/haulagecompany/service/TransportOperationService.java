package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.*;
import hu.bme.aut.haulagecompany.model.dto.StackedGoodDTO;
import hu.bme.aut.haulagecompany.model.dto.TransportOperationDTO;
import hu.bme.aut.haulagecompany.repository.TransportOperationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
        if(transportOperationRepository.findByOrderId(transportOperationDTO.getOrderDTO().getId()).isPresent()){
            return null;
        }

        List<Long> usedVehicleIDs = new ArrayList<>();
        for (var uv : transportOperationDTO.getUsedVehicleDTOs()){
            usedVehicleIDs.add(uv.getId());
        }

        if(!checkVehicleAvailable(transportOperationDTO.getDate(), usedVehicleIDs) || !checkGoodsAvailable(usedVehicleIDs, transportOperationDTO) || !checkVehicleSizesAreGood(usedVehicleIDs, transportOperationDTO)){
            return null;
        }

        transportOperation.setUsedVehicles(vehicleService.getVehiclesByIds(usedVehicleIDs));
        transportOperation.setOrder(orderService.getOrderById(transportOperationDTO.getOrderDTO().getId()));
        TransportOperation createdTransportOperation = transportOperationRepository.save(transportOperation);

        orderService.setTransportOperation(createdTransportOperation);

        var vehicles = vehicleService.getVehiclesByIds(usedVehicleIDs);
        vehicleService.addTransportOperation(vehicles, createdTransportOperation);
        lorrySiteService.removeGoods(vehicles.get(0).getLocation(), orderService.getOrderById(transportOperationDTO.getOrderDTO().getId()).getGoods());
        return convertToDTO(createdTransportOperation);
    }

    private boolean checkVehicleSizesAreGood(List<Long> usedVehicleIDs, TransportOperationDTO transportOperationDTO) {
        List<Vehicle> vehicles = vehicleService.getVehiclesByIds(usedVehicleIDs);
        double summedVehicleSize = sumAttribute(vehicles, Vehicle::getSize);
        double summedVehicleMaxWeight = sumAttribute(vehicles, Vehicle::getMaxWeight);

        Order order = orderService.getOrderById(transportOperationDTO.getOrderDTO().getId());
        List<OrderedGood> orderedGoods = order.getGoods();
        AtomicInteger db = new AtomicInteger();
        orderedGoods.forEach(g -> db.set(db.get() + g.getQuantity()));
        double summedOrderedGoodsSize = sumAttribute(orderedGoods, og -> og.getGood().getSize()) * db.get();
        double summedOrderedGoodsWeight = sumAttribute(orderedGoods, og -> og.getGood().getWeight() * db.get());
        return (summedOrderedGoodsSize <= summedVehicleSize) && (summedOrderedGoodsWeight <= summedVehicleMaxWeight);
    }

    private <T> double sumAttribute(List<T> items, ToDoubleFunction<T> attributeExtractor) {
        return items.stream()
                .mapToDouble(attributeExtractor)
                .sum();
    }

    private boolean checkGoodsAvailable(List<Long> usedVehicleIDs, TransportOperationDTO transportOperationDTO) {
        Order order = orderService.getOrderById(transportOperationDTO.getOrderDTO().getId());
        List<Vehicle> vehicles = vehicleService.getVehiclesByIds(usedVehicleIDs);
        List<LorrySite> lorrySites = new ArrayList<>();
        vehicles.forEach(v -> lorrySites.add(lorrySiteService.findById(v.getLocation().getId()).orElse(v.getLocation())));
        List<InventoryGood> lorrySiteGoods = new ArrayList<>();
        lorrySites.forEach(l -> lorrySiteGoods.addAll(l.getGoods()));
        List<OrderedGood> orderedGoods = order.getGoods();
        AtomicBoolean res = new AtomicBoolean(true);
        orderedGoods.forEach(og -> lorrySiteGoods.forEach(g -> {
            if(Objects.equals(og.getGood().getName(), g.getGood().getName()) && Objects.equals(og.getGood().getSize(), g.getGood().getSize()) && Objects.equals(og.getGood().getWeight(), g.getGood().getWeight())
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

            List<Long> usedVehicleIDs = new ArrayList<>();
            for (var uv : updatedTransportOperationDTO.getUsedVehicleDTOs()){
                usedVehicleIDs.add(uv.getId());
            }
            updatedTransportOperation.setUsedVehicles(vehicleService.getVehiclesByIds(usedVehicleIDs));

            updatedTransportOperation.setOrder(orderService.getOrderById(updatedTransportOperationDTO.getOrderDTO().getId()));

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
            var lSId = realTO.getUsedVehicles().get(0).getLocation().getId();
            if(realTO.getOrder() != null){
                for(OrderedGood g : realTO.getOrder().getGoods()){
                    lorrySiteService.addGood(lSId, convertToStackedGoodDTO(g));
                }
                Order o = realTO.getOrder();
                orderService.removeTransportOperation(o);
            }
            List<Vehicle> vl = realTO.getUsedVehicles();
            for(var v : vl){
                vehicleService.removeTransportOperation(v, tO);
            }
            realTO.setUsedVehicles(null);
        }
        transportOperationRepository.deleteById(id);
    }

    private StackedGoodDTO convertToStackedGoodDTO(OrderedGood g) {
        StackedGoodDTO sgDTO = new StackedGoodDTO();
        sgDTO.setGoodId(g.getGood().getId());
        sgDTO.setQuantity(g.getQuantity());
        return sgDTO;
    }

    private TransportOperationDTO convertToDTO(TransportOperation transportOperation) {
        TransportOperationDTO toDTO = modelMapper.map(transportOperation, TransportOperationDTO.class);
        if(transportOperation.getOrder() != null){
            toDTO.setOrderDTO(orderService.getOrderDTOById(transportOperation.getOrder().getId()));
        }
        return toDTO;
    }

    private TransportOperation convertToEntity(TransportOperationDTO transportOperationDTO) {
        return modelMapper.map(transportOperationDTO, TransportOperation.class);
    }
}
