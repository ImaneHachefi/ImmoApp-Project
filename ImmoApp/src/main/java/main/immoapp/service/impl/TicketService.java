package main.immoapp.service.impl;

import main.immoapp.dto.request.MessageRequest;
import main.immoapp.dto.request.TicketRequest;
import main.immoapp.dto.response.MessageResponse;
import main.immoapp.dto.response.TicketResponse;
import main.immoapp.entity.*;
import main.immoapp.exception.ResourceNotFoundException;
import main.immoapp.mapper.TicketMapper;
import main.immoapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketSAVRepository ticketRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    // Créer un ticket
    @Transactional
    public TicketResponse creerTicket(TicketRequest request) {
        User client = userRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

        TicketSAV ticket = TicketSAV.builder()
                .sujet(request.getSujet())
                .description(request.getDescription())
                .statut("OUVERT")
                .priorite(request.getPriorite() != null ? request.getPriorite() : "MOYENNE")
                .dateCreation(LocalDate.now())
                .client(client)
                .build();

        ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(ticket);
    }

    // Lister tous les tickets
    @Transactional(readOnly = true)
    public List<TicketResponse> listerTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::toTicketResponse)
                .collect(Collectors.toList());
    }

    // Tickets par client
    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsByClient(Long clientId) {
        return ticketRepository.findByClientId(clientId)
                .stream()
                .map(ticketMapper::toTicketResponse)
                .collect(Collectors.toList());
    }

    // Tickets par agent SAV
    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsByAgent(Long agentId) {
        return ticketRepository.findByAgentId(agentId)
                .stream()
                .map(ticketMapper::toTicketResponse)
                .collect(Collectors.toList());
    }

    // Tickets par statut
    @Transactional(readOnly = true)
    public List<TicketResponse> getTicketsByStatut(String statut) {
        return ticketRepository.findByStatut(statut)
                .stream()
                .map(ticketMapper::toTicketResponse)
                .collect(Collectors.toList());
    }

    // Obtenir ticket par ID
    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long id) {
        return ticketMapper.toTicketResponse(
                ticketRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Ticket introuvable")));
    }

    // Assigner agent SAV
    @Transactional
    public TicketResponse assignerAgent(Long ticketId, Long agentId) {
        TicketSAV ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket introuvable"));
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent introuvable"));

        ticket.setAgent(agent);
        ticket.setStatut("EN_COURS");
        ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(ticket);
    }

    // Changer statut ticket
    @Transactional
    public TicketResponse changerStatut(Long id, String statut) {
        TicketSAV ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket introuvable"));

        ticket.setStatut(statut);

        if (statut.equals("RESOLU") || statut.equals("FERME")) {
            ticket.setDateResolution(LocalDate.now());
        }

        ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(ticket);
    }

    // Ajouter un message
    @Transactional
    public MessageResponse ajouterMessage(MessageRequest request) {
        TicketSAV ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket introuvable"));

        Message message = Message.builder()
                .contenu(request.getContenu())
                .expediteur(request.getExpediteur())
                .estBot(request.getEstBot())
                .dateEnvoi(LocalDate.now())
                .ticket(ticket)
                .build();

        messageRepository.save(message);
        return ticketMapper.toMessageResponse(message);
    }

    // Messages d'un ticket
    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(Long ticketId) {
        return messageRepository.findByTicketIdOrderByDateEnvoiAsc(ticketId)
                .stream()
                .map(ticketMapper::toMessageResponse)
                .collect(Collectors.toList());
    }
}