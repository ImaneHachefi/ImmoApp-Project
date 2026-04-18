package main.immoapp.controller;

import main.immoapp.dto.request.MessageRequest;
import main.immoapp.dto.request.TicketRequest;
import main.immoapp.dto.response.MessageResponse;
import main.immoapp.dto.response.TicketResponse;
import main.immoapp.service.impl.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // CLIENT et ADMIN peuvent créer un ticket
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<TicketResponse> creerTicket(
            @Valid @RequestBody TicketRequest request) {
        return ResponseEntity.ok(ticketService.creerTicket(request));
    }

    // AGENT_SAV et ADMIN voient tous les tickets
    @GetMapping
    @PreAuthorize("hasAnyRole('AGENT_SAV', 'ADMIN')")
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        return ResponseEntity.ok(ticketService.listerTickets());
    }

    // CLIENT voit ses propres tickets
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('CLIENT', 'AGENT_SAV', 'ADMIN')")
    public ResponseEntity<List<TicketResponse>> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(ticketService.getTicketsByClient(clientId));
    }

    // AGENT_SAV voit ses tickets assignés
    @GetMapping("/agent/{agentId}")
    @PreAuthorize("hasAnyRole('AGENT_SAV', 'ADMIN')")
    public ResponseEntity<List<TicketResponse>> getByAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(ticketService.getTicketsByAgent(agentId));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('AGENT_SAV', 'ADMIN')")
    public ResponseEntity<List<TicketResponse>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(ticketService.getTicketsByStatut(statut));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'AGENT_SAV', 'ADMIN')")
    public ResponseEntity<TicketResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    // ADMIN assigne un agent SAV
    @PatchMapping("/{ticketId}/agent/{agentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketResponse> assignerAgent(
            @PathVariable Long ticketId,
            @PathVariable Long agentId) {
        return ResponseEntity.ok(ticketService.assignerAgent(ticketId, agentId));
    }

    // AGENT_SAV change le statut
    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('AGENT_SAV', 'ADMIN')")
    public ResponseEntity<TicketResponse> changerStatut(
            @PathVariable Long id,
            @RequestParam String statut) {
        return ResponseEntity.ok(ticketService.changerStatut(id, statut));
    }

    // Ajouter un message
    @PostMapping("/messages")
    @PreAuthorize("hasAnyRole('CLIENT', 'AGENT_SAV', 'ADMIN')")
    public ResponseEntity<MessageResponse> ajouterMessage(
            @Valid @RequestBody MessageRequest request) {
        return ResponseEntity.ok(ticketService.ajouterMessage(request));
    }

    // Messages d'un ticket
    @GetMapping("/{ticketId}/messages")
    @PreAuthorize("hasAnyRole('CLIENT', 'AGENT_SAV', 'ADMIN')")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.getMessages(ticketId));
    }
}