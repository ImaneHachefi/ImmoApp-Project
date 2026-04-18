package main.immoapp.controller;

import main.immoapp.dto.request.InteractionRequest;
import main.immoapp.dto.request.ProspectRequest;
import main.immoapp.dto.response.InteractionResponse;
import main.immoapp.dto.response.ProspectResponse;
import main.immoapp.service.impl.ProspectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prospects")
@RequiredArgsConstructor
public class ProspectController {

    private final ProspectService prospectService;

    @PostMapping
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ProspectResponse> creerProspect(
            @Valid @RequestBody ProspectRequest request) {
        return ResponseEntity.ok(prospectService.creerProspect(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<List<ProspectResponse>> getAllProspects() {
        return ResponseEntity.ok(prospectService.listerProspects());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ProspectResponse> getProspectById(@PathVariable Long id) {
        return ResponseEntity.ok(prospectService.getProspectById(id));
    }

    @GetMapping("/agent/{agentId}")
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<List<ProspectResponse>> getByAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(prospectService.getProspectsByAgent(agentId));
    }

    @GetMapping("/categorie/{categorie}")
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<List<ProspectResponse>> getByCategorie(@PathVariable String categorie) {
        return ResponseEntity.ok(prospectService.getProspectsByCategorie(categorie));
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ProspectResponse> modifierStatut(
            @PathVariable Long id,
            @RequestParam String statut) {
        return ResponseEntity.ok(prospectService.modifierStatut(id, statut));
    }

    @PatchMapping("/{prospectId}/agent/{agentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProspectResponse> assignerAgent(
            @PathVariable Long prospectId,
            @PathVariable Long agentId) {
        return ResponseEntity.ok(prospectService.assignerAgent(prospectId, agentId));
    }

    @PostMapping("/interactions")
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN', 'CLIENT')")
    public ResponseEntity<InteractionResponse> ajouterInteraction(
            @Valid @RequestBody InteractionRequest request) {
        return ResponseEntity.ok(prospectService.ajouterInteraction(request));
    }

    @GetMapping("/{prospectId}/interactions")
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<List<InteractionResponse>> getInteractions(
            @PathVariable Long prospectId) {
        return ResponseEntity.ok(prospectService.getInteractions(prospectId));
    }
}