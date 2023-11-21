package hu.bme.aut.haulagecompany.model.mapper;

import hu.bme.aut.haulagecompany.model.Vehicle;
import hu.bme.aut.haulagecompany.model.dto.VehicleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mm =  new ModelMapper();
        mm.typeMap(Vehicle.class, VehicleDTO.class).addMappings(mapper -> mapper.map(src -> src.getLocation().getId(), VehicleDTO::setLorrySiteID));
        return mm;
    }
}
