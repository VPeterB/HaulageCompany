package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.Order;
import hu.bme.aut.haulagecompany.model.OrderedGood;
import hu.bme.aut.haulagecompany.model.TransportOperation;
import hu.bme.aut.haulagecompany.model.dto.StackedGoodDTO;
import hu.bme.aut.haulagecompany.model.dto.VehicleDTO;
import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.repository.OrderRepository;
import hu.bme.aut.haulagecompany.repository.TransportOperationRepository;
import hu.bme.aut.haulagecompany.repository.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final TransportOperationRepository transportOperationRepository;
    private final OrderRepository orderRepository;
    private final LorrySiteService lorrySiteService;
    private final ModelMapper modelMapper;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, TransportOperationRepository transportOperationRepository, OrderRepository orderRepository, LorrySiteService lorrySiteService, ModelMapper modelMapper) {
        this.vehicleRepository = vehicleRepository;
        this.transportOperationRepository = transportOperationRepository;
        this.orderRepository = orderRepository;
        this.lorrySiteService = lorrySiteService;
        this.modelMapper = modelMapper;
    }

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        if(vehicleRepository.findByLicensePlate(vehicleDTO.getLicensePlate()).isPresent()){
            return new VehicleDTO();
        }
        Vehicle vehicle = convertToEntity(vehicleDTO);
        vehicle.setLocation((lorrySiteService.findById(vehicleDTO.getLorrySiteID())).orElse(null));
        if(vehicle.getLocation() == null){
            return null;
        }
        vehicle.setTransportOperations(new ArrayList<>());
        Vehicle createdVehicle = vehicleRepository.save(vehicle);
        lorrySiteService.addVehicle(createdVehicle);
        return convertToDTO(createdVehicle);
    }

    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = (List<Vehicle>) vehicleRepository.findAll();
        return vehicles.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public VehicleDTO getVehicleById(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.map(this::convertToDTO).orElse(null);
    }

    public VehicleDTO updateVehicle(Long id, VehicleDTO updatedVehicleDTO) {
        Optional<Vehicle> existingVehicle = vehicleRepository.findById(id);

        if (existingVehicle.isPresent()) {
            Vehicle updatedVehicle = existingVehicle.get();
            updatedVehicle.setLicensePlate(updatedVehicleDTO.getLicensePlate());
            if(updatedVehicle.getTransportOperations().isEmpty()){
                updatedVehicle.setSize(updatedVehicleDTO.getSize());
                updatedVehicle.setMaxWeight(updatedVehicleDTO.getMaxWeight());
            }
            updatedVehicle.setLocation(lorrySiteService.findById(updatedVehicleDTO.getLorrySiteID()).orElse(existingVehicle.get().getLocation()));
            Vehicle savedVehicle = vehicleRepository.save(updatedVehicle);
            /*for(TransportOperation to: savedVehicle.getTransportOperations()){ //concurent mod error
                if(to != null){
                    var lSId = to.getUsedVehicles().get(0).getLocation().getId();
                    if(to.getOrder() != null){
                        for(OrderedGood g : to.getOrder().getGoods()){
                            lorrySiteService.addGood(lSId, convertToStackedGoodDTO(g));
                        }
                        Order o = to.getOrder();
                        if(o != null){
                            Optional<Order> order = orderRepository.findById(o.getId());
                            if(order.isPresent()){
                                Order realO = order.get();
                                realO.setTransportOperation(null);
                                orderRepository.save(realO);
                            }
                        }
                    }
                    List<Vehicle> vl = to.getUsedVehicles();
                    for(var v : vl){
                        var tooo = transportOperationRepository.findById(to.getId());
                        removeTransportOperation(v, tooo);
                    }
                    to.setUsedVehicles(null);
                }
                transportOperationRepository.deleteById(id);
            }*/
            return convertToDTO(savedVehicle);
        } else {
            return null;
        }
    }

    private StackedGoodDTO convertToStackedGoodDTO(OrderedGood g) {
        StackedGoodDTO sgDTO = new StackedGoodDTO();
        sgDTO.setGoodId(g.getGood().getId());
        sgDTO.setQuantity(g.getQuantity());
        return sgDTO;
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    private Vehicle convertToEntity(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, Vehicle.class);
    }

    public List<Vehicle> getVehiclesByIds(List<Long> usedVehicleIDs) {
        return StreamSupport.stream(vehicleRepository.findAllById(usedVehicleIDs).spliterator(), false).toList();
    }

    public void addTransportOperation(List<Vehicle> vehicles, TransportOperation createdTransportOperation) {
        for(Vehicle v : vehicles){
            Optional<Vehicle> currVehicle = vehicleRepository.findById(v.getId());
            if(currVehicle.isPresent()){
                Vehicle realV = currVehicle.get();
                List<TransportOperation> tOList = realV.getTransportOperations();
                tOList.add(createdTransportOperation);
                realV.setTransportOperations(tOList);
                vehicleRepository.save(realV);
            }
        }
    }

    public void removeTransportOperation(Vehicle v, Optional<TransportOperation> to) {
        Optional<Vehicle> veh = vehicleRepository.findById(v.getId());
        if(veh.isPresent()){
            Vehicle realVehicle = veh.get();
            List<TransportOperation> toList = realVehicle.getTransportOperations();
            toList.remove(to.orElse(null));
        }
    }
}
