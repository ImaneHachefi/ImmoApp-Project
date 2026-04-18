package main.immoapp.dto.response;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChatbotResponse {

    private String message;           // message du chatbot
    private List<BienResponse> biens; // biens recommandés
    private Float scoreIA;            // score mis à jour
    private String categorieIA;       // CHAUD/TIEDE/FROID
    private String prochainQuestion;  // prochaine question du chatbot
}