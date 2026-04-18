package main.immoapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "prospect")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Prospect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lié au compte CLIENT
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Float budget;
    private String localisation;

    @Column(nullable = false)
    private String statut; // NOUVEAU, CONTACTE, INTERESSE, EN_NEGOCIATION, CONVERTI, PERDU

    private Float scoreIA;
    private String categorieIA; // CHAUD, TIEDE, FROID

    @Column(nullable = false)
    private LocalDate dateCreation;

    // Agent commercial assigné
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private User agent;

    @OneToMany(mappedBy = "prospect", cascade = CascadeType.ALL)
    private List<Interaction> interactions;

    @OneToMany(mappedBy = "prospect", cascade = CascadeType.ALL)
    private List<ProspectBien> prospectBiens;
}