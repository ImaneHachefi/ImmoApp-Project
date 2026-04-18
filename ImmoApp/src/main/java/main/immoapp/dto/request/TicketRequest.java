package main.immoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TicketRequest {

    @NotBlank(message = "Le sujet est obligatoire")
    private String sujet;

    private String description;

    private String priorite; // BASSE, MOYENNE, HAUTE

    @NotNull(message = "L'id du client est obligatoire")
    private Long clientId;
}