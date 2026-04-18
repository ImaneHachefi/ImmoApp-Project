package main.immoapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "interaction")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // LIKE, DISLIKE, QUESTION, VISITE

    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prospect_id", nullable = false)
    private Prospect prospect;
}