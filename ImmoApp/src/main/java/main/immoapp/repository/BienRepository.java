package main.immoapp.repository;

import main.immoapp.entity.BienImmobilier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BienRepository extends JpaRepository<BienImmobilier, Long> {
    List<BienImmobilier> findByDisponibleTrue();
    List<BienImmobilier> findByLocalisation(String localisation);
    List<BienImmobilier> findByPrixBetween(Float min, Float max);
    List<BienImmobilier> findByLocalisationAndPrixBetweenAndDisponibleTrue(
            String localisation, Float min, Float max);
}