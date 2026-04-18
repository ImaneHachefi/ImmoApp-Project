package main.immoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MessageRequest {

    @NotNull(message = "L'id du ticket est obligatoire")
    private Long ticketId;

    @NotBlank(message = "Le contenu est obligatoire")
    private String contenu;

    @NotBlank(message = "L'expéditeur est obligatoire")
    private String expediteur; // CLIENT, AGENT_SAV, BOT

    private Boolean estBot = false;
}