package main.immoapp.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProspectResponse {
    private Long id;
    private String nomClient;
    private String emailClient;
    private Float budget;
    private String localisation;
    private String statut;
    private Float scoreIA;
    private String categorieIA;
    private LocalDate dateCreation;
    private String nomAgent;
}