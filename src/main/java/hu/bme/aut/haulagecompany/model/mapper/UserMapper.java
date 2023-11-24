package hu.bme.aut.haulagecompany.model.mapper;

import hu.bme.aut.haulagecompany.model.Role;
import hu.bme.aut.haulagecompany.model.User;
import hu.bme.aut.haulagecompany.model.dto.UserDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDTO(User user);

    @AfterMapping
    default void getRoles(@MappingTarget UserDTO target, User source) {
        List<String> roles = new ArrayList<>();
        if(source.getRoles() != null){
            for(Role role : source.getRoles()){
                roles.add(role.getName());
            }
        }
        target.setRoles(roles);
    }

    default List<String> mapRolesToStrings(List<Role> roles) {
        List<String> roleNames = new ArrayList<>();
        if (roles != null) {
            for (Role role : roles) {
                roleNames.add(role.getName());
            }
        }
        return roleNames;
    }
}
