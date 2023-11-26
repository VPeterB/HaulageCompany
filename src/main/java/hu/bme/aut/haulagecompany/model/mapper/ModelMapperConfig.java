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
            mapper.map(Vehicle::getTOIDs, VehicleDTO::setTransportOperationIDs);
        });

        mm.typeMap(TransportOperation.class, TransportOperationDTO.class).addMappings(mapper-> mapper.map(src -> mapVehiclesToVehicleDTOs(src.getUsedVehicles(), mm), TransportOperationDTO::setUsedVehicleDTOs));

        mm.typeMap(Shop.class, ShopDTO.class).addMappings(mapper-> mapper.map(Shop::getOrderIDs, ShopDTO::setOrderIDs));

        mm.typeMap(Order.class, GetOrderDTO.class).addMappings(mapper -> mapper.map(src -> mapOrderedGoodsToStackedGoodDTOs(src.getGoods(), mm), GetOrderDTO::setGoodDTOs));

        mm.typeMap(LorrySite.class, LorrySiteDTO.class).addMappings(mapper -> {
            mapper.map(src -> mapVehiclesToVehicleDTOs(src.getVehicles(), mm), LorrySiteDTO::setVehicleDTOs);
            mapper.map(src -> mapInventoryGoodsToStackedGoodDTOs(src.getGoods(), mm), LorrySiteDTO::setGoodDTOs);
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

    private List<StackedGoodDTO> mapInventoryGoodsToStackedGoodDTOs(List<InventoryGood> goods, ModelMapper mm) {
        if(goods != null){
            return goods.stream()
                    .map(good -> mm.map(good, StackedGoodDTO.class))
                    .toList();
        }
        return new ArrayList<>();
    }

    private List<StackedGoodDTO> mapOrderedGoodsToStackedGoodDTOs(List<OrderedGood> goods, ModelMapper mm) {
        if(goods != null){
            return goods.stream()
                    .map(good -> mm.map(good, StackedGoodDTO.class))
                    .toList();
        }
        return new ArrayList<>();
    }
}
