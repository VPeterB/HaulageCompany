package hu.bme.aut.haulagecompany.model.mapper;

import hu.bme.aut.haulagecompany.model.*;
import hu.bme.aut.haulagecompany.model.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm =  new ModelMapper();

        mm.typeMap(Vehicle.class, VehicleDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getLocation().getId(), VehicleDTO::setLorrySiteID);
            mapper.map(src -> mapTransportOperationsToTransportOperationDTOs(src.getTransportOperations(), mm), VehicleDTO::setTransportOperationDTOs);
        });

        mm.typeMap(TransportOperation.class, TransportOperationDTO.class).addMappings(mapper-> mapper.map(TransportOperation::getUsedVehicleIds, TransportOperationDTO::setUsedVehicleIDs));

        mm.typeMap(Shop.class, ShopDTO.class).addMappings(mapper-> mapper.map(Shop::getOrderIDs, ShopDTO::setOrderIDs));

        mm.typeMap(Order.class, GetOrderDTO.class).addMappings(mapper -> mapper.map(src -> mapGoodsToGoodDTOs(src.getGoods(), mm), GetOrderDTO::setGoodDTOs));

        mm.typeMap(LorrySite.class, LorrySiteDTO.class).addMappings(mapper -> {
            mapper.map(src -> mapVehiclesToVehicleDTOs(src.getVehicles(), mm), LorrySiteDTO::setVehicleDTOs);
            mapper.map(src -> mapGoodsToGoodDTOs(src.getGoods(), mm), LorrySiteDTO::setGoodDTOs);
        });

        return mm;
    }

    private List<VehicleDTO> mapVehiclesToVehicleDTOs(List<Vehicle> vehicles, ModelMapper mm) {
        if(vehicles != null){
            return vehicles.stream()
                    .map(vehicle -> mm.map(vehicle, VehicleDTO.class))
                    .toList();
        }
        return new ArrayList<>();
    }

    private List<TransportOperationDTO> mapTransportOperationsToTransportOperationDTOs(List<TransportOperation> transportOperations, ModelMapper mm) {
        if(transportOperations != null){
            return transportOperations.stream()
                    .map(to -> mm.map(to, TransportOperationDTO.class))
                    .toList();
        }
        return new ArrayList<>();
    }

    private List<GoodDTO> mapGoodsToGoodDTOs(List<Good> goods, ModelMapper mm) {
        if(goods != null){
            return goods.stream()
                    .map(good -> mm.map(good, GoodDTO.class))
                    .toList();
        }
        return new ArrayList<>();
    }
}
