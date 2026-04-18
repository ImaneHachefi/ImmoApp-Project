package main.immoapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "photo")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    private Integer ordre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bien_id", nullable = false)
    private BienImmobilier bien;
}