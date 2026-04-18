package main.immoapp.mapper;

import main.immoapp.dto.response.MessageResponse;
import main.immoapp.dto.response.TicketResponse;
import main.immoapp.entity.Message;
import main.immoapp.entity.TicketSAV;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(source = "client.nom", target = "nomClient")
    @Mapping(source = "agent.nom", target = "nomAgent")
    TicketResponse toTicketResponse(TicketSAV ticket);

    MessageResponse toMessageResponse(Message message);
}