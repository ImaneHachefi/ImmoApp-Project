package main.immoapp.repository;
import main.immoapp.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
    List<Interaction> findByProspectIdOrderByDateDesc(Long prospectId);
    List<Interaction> findByProspectIdAndType(Long prospectId, String type);
    long countByProspectId(Long prospectId);
}