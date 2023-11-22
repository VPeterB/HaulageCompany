package hu.bme.aut.haulagecompany.model.mapper;

import hu.bme.aut.haulagecompany.model.*;
import hu.bme.aut.haulagecompany.model.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm =  new ModelMapper();

        mm.typeMap(Vehicle.class, VehicleDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getLocation().getId(), VehicleDTO::setLorrySiteID);
            mapper.map(Vehicle::getTransportOperationIds, VehicleDTO::setTransportOperationIDs);
        });

        mm.typeMap(TransportOperation.class, TransportOperationDTO.class).addMappings(mapper-> {
            mapper.map(src -> {
                if(src.getOrder() == null) return null;
                return src.getOrder().getId();
            }, TransportOperationDTO::setOrderID);
            mapper.map(TransportOperation::getUsedVehicleIds, TransportOperationDTO::setUsedVehicleIDs);
        });

        mm.typeMap(Shop.class, ShopDTO.class).addMappings(mapper-> mapper.map(Shop::getOrderIDs, ShopDTO::setOrderIDs));

        mm.typeMap(Order.class, OrderDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getShop().getId(), OrderDTO::setShopID);
            mapper.map(src -> {
                if(src.getTransportOperation() == null) return null;
                return src.getTransportOperation().getId();
            }, OrderDTO::setTransportOperationID);
            mapper.map(Order::getGoodIds, OrderDTO::setGoodIDs);
        });

        mm.typeMap(LorrySite.class, LorrySiteDTO.class).addMappings(mapper -> {
            mapper.map(LorrySite::getVehicleIds, LorrySiteDTO::setVehicleIDs);
            mapper.map(LorrySite::getGoodIds, LorrySiteDTO::setGoodIDs);
        });

        return mm;
    }
}
