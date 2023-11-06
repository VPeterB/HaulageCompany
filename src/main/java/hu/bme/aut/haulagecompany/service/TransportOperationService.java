package hu.bme.aut.haulagecompany.service;

import hu.bme.aut.haulagecompany.model.TransportOperation;
import hu.bme.aut.haulagecompany.model.TransportedGood;
import hu.bme.aut.haulagecompany.model.dto.TransportOperationDTO;
import hu.bme.aut.haulagecompany.model.dto.StoredGoodDTO;
import hu.bme.aut.haulagecompany.repository.TransportOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransportOperationService {
    @Autowired
    private TransportOperationRepository transportOperationRepository;

    public TransportOperation createTransportOperation(TransportOperationDTO transportOperationDTO) {
        TransportOperation transportOperation = new TransportOperation();
        transportOperation.setVehicleId(transportOperationDTO.getVehicleId());
        transportOperation.setShopId(transportOperationDTO.getShopId());

        List<StoredGoodDTO> transportedGoodsDTO = transportOperationDTO.getGoods();
        List<TransportedGood> transportedGoods = new ArrayList<>();
        for (StoredGoodDTO storedGoodDTO : transportedGoodsDTO) {
            TransportedGood transportedGood = new TransportedGood();
            transportedGood.setGoodId(storedGoodDTO.getGoodId());
            transportedGood.setQuantity(storedGoodDTO.getQuantity());
            transportedGoods.add(transportedGood);
        }
        transportOperation.setGoods(transportedGoods);

        return transportOperationRepository.save(transportOperation);
    }

    public Iterable<TransportOperation> getAllTransportOperations() {
        return transportOperationRepository.findAll();
    }

    public TransportOperation getTransportOperationById(Long id) {
        return transportOperationRepository.findById(id).orElse(null);
    }

    public TransportOperation updateTransportOperation(Long id, TransportOperationDTO transportOperationDTO) {
        TransportOperation existingTransportOperation = getTransportOperationById(id);
        if (existingTransportOperation != null) {
            existingTransportOperation.setVehicleId(transportOperationDTO.getVehicleId());
            existingTransportOperation.setShopId(transportOperationDTO.getShopId());

            List<StoredGoodDTO> transportedGoodsDTO = transportOperationDTO.getGoods();
            List<TransportedGood> transportedGoods = new ArrayList<>();
            for (StoredGoodDTO storedGoodDTO : transportedGoodsDTO) {
                TransportedGood transportedGood = new TransportedGood();
                transportedGood.setGoodId(storedGoodDTO.getGoodId());
                transportedGood.setQuantity(storedGoodDTO.getQuantity());
                transportedGoods.add(transportedGood);
            }
            existingTransportOperation.setGoods(transportedGoods);

            return transportOperationRepository.save(existingTransportOperation);
        }
        return null;
    }

    public void deleteTransportOperation(Long id) {
        transportOperationRepository.deleteById(id);
    }
}
