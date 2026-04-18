package main.immoapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String motDePasse;

    // Par défaut CLIENT, l'admin peut changer
    private String role = "CLIENT";
}
