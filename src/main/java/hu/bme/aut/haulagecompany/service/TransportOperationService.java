package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.TransportOperation;
import hu.bme.aut.haulagecompany.model.dto.TransportOperationDTO;
import hu.bme.aut.haulagecompany.repository.TransportOperationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransportOperationService {
    private final TransportOperationRepository transportOperationRepository;
    private final ModelMapper modelMapper;
    private final VehicleService vehicleService;
    private final OrderService orderService;

    @Autowired
    public TransportOperationService(
            TransportOperationRepository transportOperationRepository,
            VehicleService vehicleService,
            OrderService orderService) {
        this.transportOperationRepository = transportOperationRepository;
        this.modelMapper = new ModelMapper();
        this.vehicleService = vehicleService;
        this.orderService = orderService;
    }

    public TransportOperationDTO createTransportOperation(TransportOperationDTO transportOperationDTO) {
        TransportOperation transportOperation = convertToEntity(transportOperationDTO);
        transportOperation.setUsedVehicles(vehicleService.getVehiclesByIds(transportOperationDTO.getUsedVehicleIDs()));
        transportOperation.setOrder(orderService.getOrderById(transportOperationDTO.getOrderID()));

        TransportOperation createdTransportOperation = transportOperationRepository.save(transportOperation);
        return convertToDTO(createdTransportOperation);
    }

    public List<TransportOperationDTO> getAllTransportOperations() {
        List<TransportOperation> transportOperations = (List<TransportOperation>) transportOperationRepository.findAll();
        return transportOperations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TransportOperationDTO getTransportOperationDTOById(Long id) {
        Optional<TransportOperation> transportOperation = transportOperationRepository.findById(id);
        return transportOperation.map(this::convertToDTO).orElse(null);
    }

    public TransportOperation getTransportOperationById(Long id) {
        Optional<TransportOperation> transportOperation = transportOperationRepository.findById(id);
        return transportOperation.orElse(null);
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
        transportOperationRepository.deleteById(id);
    }

    private TransportOperationDTO convertToDTO(TransportOperation transportOperation) {
        return modelMapper.map(transportOperation, TransportOperationDTO.class);
    }

    private TransportOperation convertToEntity(TransportOperationDTO transportOperationDTO) {
        return modelMapper.map(transportOperationDTO, TransportOperation.class);
    }
}
