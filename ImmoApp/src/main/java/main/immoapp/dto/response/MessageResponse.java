package main.immoapp.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MessageResponse {
    private Long id;
    private String contenu;
    private String expediteur;
    private Boolean estBot;
    private LocalDate dateEnvoi;
}