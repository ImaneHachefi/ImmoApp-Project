package main.immoapp.controller;

import main.immoapp.dto.request.BienRequest;
import main.immoapp.dto.response.BienResponse;
import main.immoapp.service.impl.BienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biens")
@RequiredArgsConstructor
public class BienController {

    private final BienService bienService;

    // AGENT_COMMERCIAL et ADMIN peuvent créer
    @PostMapping
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<BienResponse> creerBien(@Valid @RequestBody BienRequest request) {
        return ResponseEntity.ok(bienService.creerBien(request));
    }

    // Tout le monde peut voir les biens disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<BienResponse>> getBiensDisponibles() {
        return ResponseEntity.ok(bienService.listerBiensDisponibles());
    }

    // ADMIN et AGENT_COMMERCIAL voient tous les biens
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT_COMMERCIAL')")
    public ResponseEntity<List<BienResponse>> getAllBiens() {
        return ResponseEntity.ok(bienService.listerBiens());
    }

    // Tout le monde peut voir un bien par ID
    @GetMapping("/{id}")
    public ResponseEntity<BienResponse> getBienById(@PathVariable Long id) {
        return ResponseEntity.ok(bienService.getBienById(id));
    }

    // AGENT_COMMERCIAL et ADMIN peuvent modifier
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AGENT_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<BienResponse> modifierBien(
            @PathVariable Long id,
            @Valid @RequestBody BienRequest request) {
        return ResponseEntity.ok(bienService.modifierBien(id, request));
    }

    // Seul ADMIN peut supprimer
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> supprimerBien(@PathVariable Long id) {
        bienService.supprimerBien(id);
        return ResponseEntity.noContent().build();
    }

    // Recherche par localisation et prix — accessible à tous
    @GetMapping("/recherche")
    public ResponseEntity<List<BienResponse>> rechercherBiens(
            @RequestParam String localisation,
            @RequestParam Float prixMin,
            @RequestParam Float prixMax) {
        return ResponseEntity.ok(bienService.rechercherBiens(localisation, prixMin, prixMax));
    }
}