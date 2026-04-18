package main.immoapp.service.impl;

import main.immoapp.dto.request.InteractionRequest;
import main.immoapp.dto.request.ProspectRequest;
import main.immoapp.dto.response.InteractionResponse;
import main.immoapp.dto.response.ProspectResponse;
import main.immoapp.entity.*;
import main.immoapp.exception.ResourceNotFoundException;
import main.immoapp.mapper.ProspectMapper;
import main.immoapp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProspectService {

    private final ProspectRepository prospectRepository;
    private final InteractionRepository interactionRepository;
    private final UserRepository userRepository;
    private final ProspectMapper prospectMapper;
    private final IAService iaService;

    // Créer un prospect
    @Transactional
    public ProspectResponse creerProspect(ProspectRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        if (prospectRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new RuntimeException("Cet utilisateur est déjà un prospect");
        }

        User agent = null;
        if (request.getAgentId() != null) {
            agent = userRepository.findById(request.getAgentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agent introuvable"));
        }

        Prospect prospect = Prospect.builder()
                .user(user)
                .budget(request.getBudget())
                .localisation(request.getLocalisation())
                .statut(request.getStatut())
                .scoreIA(0f)
                .categorieIA("FROID")
                .dateCreation(LocalDate.now())
                .agent(agent)
                .build();

        prospectRepository.save(prospect);

        // Calcul score IA dès la création
        mettreAJourScoreIA(prospect);

        return prospectMapper.toProspectResponse(prospect);
    }

    // Lister tous les prospects
    @Transactional(readOnly = true)
    public List<ProspectResponse> listerProspects() {
        return prospectRepository.findAll()
                .stream()
                .map(prospectMapper::toProspectResponse)
                .collect(Collectors.toList());
    }

    // Prospects par agent
    @Transactional(readOnly = true)
    public List<ProspectResponse> getProspectsByAgent(Long agentId) {
        return prospectRepository.findByAgentId(agentId)
                .stream()
                .map(prospectMapper::toProspectResponse)
                .collect(Collectors.toList());
    }

    // Prospects par catégorie IA
    @Transactional(readOnly = true)
    public List<ProspectResponse> getProspectsByCategorie(String categorie) {
        return prospectRepository.findByCategorieIA(categorie)
                .stream()
                .map(prospectMapper::toProspectResponse)
                .collect(Collectors.toList());
    }

    // Obtenir un prospect par ID
    @Transactional(readOnly = true)
    public ProspectResponse getProspectById(Long id) {
        return prospectMapper.toProspectResponse(
                prospectRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Prospect introuvable")));
    }

    // Modifier statut prospect
    @Transactional
    public ProspectResponse modifierStatut(Long id, String statut) {
        Prospect prospect = prospectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prospect introuvable"));

        prospect.setStatut(statut);
        prospectRepository.save(prospect);

        // Recalcul score IA après changement de statut
        mettreAJourScoreIA(prospect);

        return prospectMapper.toProspectResponse(prospect);
    }

    // Assigner un agent
    @Transactional
    public ProspectResponse assignerAgent(Long prospectId, Long agentId) {
        Prospect prospect = prospectRepository.findById(prospectId)
                .orElseThrow(() -> new ResourceNotFoundException("Prospect introuvable"));
        User agent = userRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Agent introuvable"));
        prospect.setAgent(agent);
        prospectRepository.save(prospect);
        return prospectMapper.toProspectResponse(prospect);
    }

    // Ajouter une interaction
    @Transactional
    public InteractionResponse ajouterInteraction(InteractionRequest request) {
        Prospect prospect = prospectRepository.findById(request.getProspectId())
                .orElseThrow(() -> new ResourceNotFoundException("Prospect introuvable"));

        Interaction interaction = Interaction.builder()
                .type(request.getType())
                .contenu(request.getContenu())
                .date(LocalDate.now())
                .prospect(prospect)
                .build();

        interactionRepository.save(interaction);

        // Recalcul score IA via microservice après chaque interaction
        mettreAJourScoreIA(prospect);

        return prospectMapper.toInteractionResponse(interaction);
    }

    // Calcul Score IA via Microservice Python
    @Transactional
    public void mettreAJourScoreIA(Prospect prospect) {

        try {
            // Récupérer toutes les interactions du prospect
            List<Interaction> interactions = interactionRepository
                    .findByProspectIdOrderByDateDesc(prospect.getId());

            // Compter par type
            int nbLikes = (int) interactions.stream()
                    .filter(i -> "LIKE".equals(i.getType())).count();
            int nbDislikes = (int) interactions.stream()
                    .filter(i -> "DISLIKE".equals(i.getType())).count();
            int nbVisites = (int) interactions.stream()
                    .filter(i -> "VISITE".equals(i.getType())).count();

            // Construire la requête pour le microservice IA
            IAService.ScoreRequest scoreRequest = new IAService.ScoreRequest(
                    prospect.getBudget() != null ? prospect.getBudget() : 0f,
                    prospect.getLocalisation() != null ? prospect.getLocalisation() : "",
                    interactions.size(),
                    nbLikes,
                    nbDislikes,
                    nbVisites,
                    prospect.getStatut() != null ? prospect.getStatut() : "NOUVEAU"
            );

            // Appel au microservice IA Python
            IAService.ScoreResponse scoreResponse = iaService.calculerScore(scoreRequest);

            // Mettre à jour le prospect avec le score IA
            prospect.setScoreIA(scoreResponse.getScore());
            prospect.setCategorieIA(scoreResponse.getCategorie());

            log.info("✅ Score IA mis à jour pour prospect {} : score={} categorie={}",
                    prospect.getId(),
                    scoreResponse.getScore(),
                    scoreResponse.getCategorie());

        } catch (Exception e) {
            // Si le microservice IA est indisponible → fallback local
            log.warn("⚠️ Microservice IA indisponible, calcul local : {}", e.getMessage());
            calculerScoreLocal(prospect);
        }

        prospectRepository.save(prospect);
    }

    // Fallback — Calcul local si microservice IA indisponible
    private void calculerScoreLocal(Prospect prospect) {
        List<Interaction> interactions = interactionRepository
                .findByProspectIdOrderByDateDesc(prospect.getId());

        float score = 0f;

        for (Interaction i : interactions) {
            switch (i.getType()) {
                case "LIKE"     -> score += 20f;
                case "DISLIKE"  -> score -= 10f;
                case "QUESTION" -> score += 15f;
                case "VISITE"   -> score += 10f;
            }
        }

        score = Math.max(0f, Math.min(100f, score));
        prospect.setScoreIA(score);

        if (score >= 70)      prospect.setCategorieIA("CHAUD");
        else if (score >= 40) prospect.setCategorieIA("TIEDE");
        else                  prospect.setCategorieIA("FROID");

        log.info("📊 Score local calculé pour prospect {} : score={} categorie={}",
                prospect.getId(), score, prospect.getCategorieIA());
    }

    // Historique interactions d'un prospect
    @Transactional(readOnly = true)
    public List<InteractionResponse> getInteractions(Long prospectId) {
        return interactionRepository.findByProspectIdOrderByDateDesc(prospectId)
                .stream()
                .map(prospectMapper::toInteractionResponse)
                .collect(Collectors.toList());
    }
}