package main.immoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProspectRequest {

    @NotNull(message = "L'id de l'utilisateur est obligatoire")
    private Long userId;

    private Float budget;
    private String localisation;

    @NotBlank(message = "Le statut est obligatoire")
    private String statut; // NOUVEAU, CONTACTE, INTERESSE, EN_NEGOCIATION, CONVERTI, PERDU

    private Long agentId;
}