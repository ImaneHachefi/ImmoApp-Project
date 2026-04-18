package main.immoapp.repository;
import main.immoapp.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByTicketIdOrderByDateEnvoiAsc(Long ticketId);
}