package main.immoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BienRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    private Float prix;

    @NotNull(message = "La disponibilité est obligatoire")
    private Boolean disponible;

    @NotBlank(message = "La localisation est obligatoire")
    private String localisation;

    private Float superficie;

    // URLs des photos
    private List<String> photos;
}