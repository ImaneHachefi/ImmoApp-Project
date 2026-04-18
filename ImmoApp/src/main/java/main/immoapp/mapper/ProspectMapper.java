package main.immoapp.mapper;

import main.immoapp.dto.response.InteractionResponse;
import main.immoapp.dto.response.ProspectResponse;
import main.immoapp.entity.Interaction;
import main.immoapp.entity.Prospect;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProspectMapper {

    @Mapping(source = "user.nom", target = "nomClient")
    @Mapping(source = "user.email", target = "emailClient")
    @Mapping(source = "agent.nom", target = "nomAgent")
    ProspectResponse toProspectResponse(Prospect prospect);

    @Mapping(source = "prospect.id", target = "prospectId")
    InteractionResponse toInteractionResponse(Interaction interaction);
}