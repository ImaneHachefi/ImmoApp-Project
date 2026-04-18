package main.immoapp.repository;

import main.immoapp.entity.ProspectBien;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProspectBienRepository extends JpaRepository<ProspectBien, Long> {
    List<ProspectBien> findByProspectId(Long prospectId);
    List<ProspectBien> findByBienId(Long bienId);
    boolean existsByProspectIdAndBienId(Long prospectId, Long bienId);
}