package main.immoapp.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class InteractionResponse {
    private Long id;
    private String type;
    private String contenu;
    private LocalDate date;
    private Long prospectId;
}