package main.immoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ChatbotRequest {

    @NotNull(message = "L'id du client est obligatoire")
    private Long clientId;

    // Préférences collectées via conversation
    private Float budgetMin;
    private Float budgetMax;
    private String localisation;
    private Float superficieMin;
    private Float superficieMax;

    // Réaction sur un bien proposé
    private Long bienId;        // bien concerné
    private String reaction;    // LIKE, DISLIKE, QUESTION
}