package main.immoapp.repository;
import main.immoapp.entity.TicketSAV;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketSAVRepository extends JpaRepository<TicketSAV, Long> {
    List<TicketSAV> findByClientId(Long clientId);
    List<TicketSAV> findByAgentId(Long agentId);
    List<TicketSAV> findByStatut(String statut);
    long countByStatut(String statut);
}