package main.immoapp.dto.response;

import lombok.*;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DashboardResponse {

    // Prospects
    private Long totalProspects;
    private Long prospectsChauds;
    private Long prospectsTièdes;
    private Long prospectsFroids;
    private Map<String, Long> prospectsByStatut;

    // Biens
    private Long totalBiens;
    private Long biensDisponibles;

    // Tickets
    private Long totalTickets;
    private Long ticketsOuverts;
    private Long ticketsEnCours;
    private Long ticketsResolus;
    private Long ticketsFermes;

    // Agents
    private Map<String, Long> ticketsByAgent;
    private Map<String, Long> prospectsByAgent;
}