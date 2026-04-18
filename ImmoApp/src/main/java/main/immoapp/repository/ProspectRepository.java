package main.immoapp.repository;

import main.immoapp.entity.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProspectRepository extends JpaRepository<Prospect, Long> {
    Optional<Prospect> findByUserId(Long userId);
    List<Prospect> findByAgentId(Long agentId);
    List<Prospect> findByCategorieIA(String categorieIA);
    List<Prospect> findByStatut(String statut);
}