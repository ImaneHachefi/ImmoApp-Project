package main.immoapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "bien_immobilier")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BienImmobilier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Float prix;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(nullable = false)
    private String localisation;

    private Float superficie;

    @Column(nullable = false)
    private LocalDate dateAjout;

    @OneToMany(
            mappedBy = "bien",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @OrderBy("ordre ASC")
    private List<Photo> photos;

    @OneToMany(
            mappedBy = "bien",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<ProspectBien> prospectBiens;
}