package main.immoapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prospect_bien")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProspectBien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prospect_id", nullable = false)
    private Prospect prospect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bien_id", nullable = false)
    private BienImmobilier bien;
}