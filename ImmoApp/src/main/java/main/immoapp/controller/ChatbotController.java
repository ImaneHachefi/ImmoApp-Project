package main.immoapp.controller;

import main.immoapp.dto.request.ChatbotRequest;
import main.immoapp.dto.response.ChatbotResponse;
import main.immoapp.service.impl.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    // Démarrer une conversation
    @GetMapping("/demarrer/{clientId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ChatbotResponse> demarrer(
            @PathVariable Long clientId) {
        return ResponseEntity.ok(
                chatbotService.demarrerConversation(clientId));
    }

    // Envoyer les préférences et recevoir des recommandations
    @PostMapping("/recommander")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ChatbotResponse> recommander(
            @Valid @RequestBody ChatbotRequest request) {
        return ResponseEntity.ok(
                chatbotService.recommanderBiens(request));
    }

    // Enregistrer la réaction sur un bien
    @PostMapping("/reaction")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ChatbotResponse> reaction(
            @Valid @RequestBody ChatbotRequest request) {
        return ResponseEntity.ok(
                chatbotService.enregistrerReaction(request));
    }
}