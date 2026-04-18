package main.immoapp.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String nom;
    private String email;
    private String role;
}