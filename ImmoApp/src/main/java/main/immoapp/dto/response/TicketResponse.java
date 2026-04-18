package main.immoapp.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TicketResponse {
    private Long id;
    private String sujet;
    private String description;
    private String statut;
    private String priorite;
    private LocalDate dateCreation;
    private LocalDate dateResolution;
    private String nomClient;
    private String nomAgent;
    private List<MessageResponse> messages;
}