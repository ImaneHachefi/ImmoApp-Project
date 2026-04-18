package main.immoapp.service.impl;

import main.immoapp.dto.response.DashboardResponse;
import main.immoapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProspectRepository prospectRepository;
    private final BienRepository bienRepository;
    private final TicketSAVRepository ticketRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {

        // ── Prospects ──
        Long totalProspects = prospectRepository.count();
        Long prospectsChauds = (long) prospectRepository
                .findByCategorieIA("CHAUD").size();
        Long prospectsTièdes = (long) prospectRepository
                .findByCategorieIA("TIEDE").size();
        Long prospectsFroids = (long) prospectRepository
                .findByCategorieIA("FROID").size();

        Map<String, Long> prospectsByStatut = prospectRepository
                .findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getStatut(),
                        Collectors.counting()
                ));

        // ── Biens ──
        Long totalBiens = bienRepository.count();
        Long biensDisponibles = (long) bienRepository
                .findByDisponibleTrue().size();

        // ── Tickets ──
        Long totalTickets = ticketRepository.count();
        Long ticketsOuverts = ticketRepository.countByStatut("OUVERT");
        Long ticketsEnCours = ticketRepository.countByStatut("EN_COURS");
        Long ticketsResolus = ticketRepository.countByStatut("RESOLU");
        Long ticketsFermes  = ticketRepository.countByStatut("FERME");

        // ── Tickets par agent ──
        Map<String, Long> ticketsByAgent = ticketRepository
                .findAll()
                .stream()
                .filter(t -> t.getAgent() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getAgent().getNom(),
                        Collectors.counting()
                ));

        // ── Prospects par agent ──
        Map<String, Long> prospectsByAgent = prospectRepository
                .findAll()
                .stream()
                .filter(p -> p.getAgent() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getAgent().getNom(),
                        Collectors.counting()
                ));

        return DashboardResponse.builder()
                .totalProspects(totalProspects)
                .prospectsChauds(prospectsChauds)
                .prospectsTièdes(prospectsTièdes)
                .prospectsFroids(prospectsFroids)
                .prospectsByStatut(prospectsByStatut)
                .totalBiens(totalBiens)
                .biensDisponibles(biensDisponibles)
                .totalTickets(totalTickets)
                .ticketsOuverts(ticketsOuverts)
                .ticketsEnCours(ticketsEnCours)
                .ticketsResolus(ticketsResolus)
                .ticketsFermes(ticketsFermes)
                .ticketsByAgent(ticketsByAgent)
                .prospectsByAgent(prospectsByAgent)
                .build();
    }
}