package main.immoapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ticket_sav")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TicketSAV {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sujet;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String statut; // OUVERT, EN_COURS, RESOLU, FERME

    private String priorite; // BASSE, MOYENNE, HAUTE

    @Column(nullable = false)
    private LocalDate dateCreation;

    private LocalDate dateResolution;

    // Client qui a ouvert le ticket
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    // Agent SAV assigné
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private User agent;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Message> messages;
}