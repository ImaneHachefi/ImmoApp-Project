package main.immoapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "message")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenu;

    @Column(nullable = false)
    private String expediteur; // "CLIENT", "AGENT_SAV", "BOT"

    @Column(nullable = false)
    private Boolean estBot;

    @Column(nullable = false)
    private LocalDate dateEnvoi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketSAV ticket;
}