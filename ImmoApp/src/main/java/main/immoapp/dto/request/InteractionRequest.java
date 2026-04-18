package main.immoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class InteractionRequest {

    @NotNull(message = "L'id du prospect est obligatoire")
    private Long prospectId;

    @NotBlank(message = "Le type est obligatoire")
    private String type; // LIKE, DISLIKE, QUESTION, VISITE

    private String contenu;
}