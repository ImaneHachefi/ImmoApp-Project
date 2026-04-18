package main.immoapp.mapper;

import main.immoapp.dto.response.AuthResponse;
import main.immoapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.nom", target = "role")
    AuthResponse toAuthResponse(User user);
}