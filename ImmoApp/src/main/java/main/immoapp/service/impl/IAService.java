package main.immoapp.service.impl;

import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class IAService {

    private final WebClient.Builder webClientBuilder;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class ScoreRequest {
        private Float budget;
        private String localisation;
        private Integer nbInteractions;
        private Integer nbLikes;
        private Integer nbDislikes;
        private Integer nbVisites;
        private String statut;
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class ScoreResponse {
        private Float score;
        private String categorie;
    }

    public ScoreResponse calculerScore(ScoreRequest request) {
        return webClientBuilder
                .baseUrl("http://localhost:8000")
                .build()
                .post()
                .uri("/score")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ScoreResponse.class)
                .block();
    }
}