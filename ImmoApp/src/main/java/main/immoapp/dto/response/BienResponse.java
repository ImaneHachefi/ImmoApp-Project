package main.immoapp.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BienResponse {
    private Long id;
    private String titre;
    private String description;
    private Float prix;
    private Boolean disponible;
    private String localisation;
    private Float superficie;
    private LocalDate dateAjout;
    private List<PhotoResponse> photos;
}