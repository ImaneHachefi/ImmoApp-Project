package main.immoapp.service.impl;

import main.immoapp.dto.request.ChatbotRequest;
import main.immoapp.dto.response.BienResponse;
import main.immoapp.dto.response.ChatbotResponse;
import main.immoapp.entity.*;
import main.immoapp.exception.ResourceNotFoundException;
import main.immoapp.mapper.BienMapper;
import main.immoapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final BienRepository bienRepository;
    private final ProspectRepository prospectRepository;
    private final InteractionRepository interactionRepository;
    private final UserRepository userRepository;
    private final BienMapper bienMapper;

    // Démarrer une conversation
    @Transactional
    public ChatbotResponse demarrerConversation(Long clientId) {

        // Vérifie si le client a déjà un profil prospect
        boolean estProspect = prospectRepository
                .findByUserId(clientId).isPresent();

        if (estProspect) {
            Prospect prospect = prospectRepository
                    .findByUserId(clientId).get();

            // Enregistrer la visite
            enregistrerInteraction(prospect, "VISITE",
                    "Client revient sur le chatbot");

            return ChatbotResponse.builder()
                    .message("Bon retour ! Voici de nouvelles recommandations basées sur vos préférences.")
                    .scoreIA(prospect.getScoreIA())
                    .categorieIA(prospect.getCategorieIA())
                    .prochainQuestion("Votre budget est-il toujours entre "
                            + prospect.getBudget() + " DH ?")
                    .build();
        }

        return ChatbotResponse.builder()
                .message("Bonjour ! Je suis votre assistant immobilier. Je vais vous aider à trouver le bien idéal !")
                .prochainQuestion("Quel est votre budget maximum ?")
                .build();
    }

    // Recommander des biens selon les préférences
    @Transactional
    public ChatbotResponse recommanderBiens(ChatbotRequest request) {

        User client = userRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

        // Récupérer ou créer le prospect
        Prospect prospect = prospectRepository
                .findByUserId(request.getClientId())
                .orElseGet(() -> creerProspectAutomatique(client,
                        request.getBudgetMax(),
                        request.getLocalisation()));

        // Mettre à jour les préférences
        if (request.getBudgetMax() != null)
            prospect.setBudget(request.getBudgetMax());
        if (request.getLocalisation() != null)
            prospect.setLocalisation(request.getLocalisation());

        prospectRepository.save(prospect);

        // Enregistrer interaction QUESTION
        enregistrerInteraction(prospect, "QUESTION",
                "Recherche: budget=" + request.getBudgetMax()
                        + " localisation=" + request.getLocalisation());

        // Rechercher les biens correspondants
        List<BienResponse> biensRecommandes = rechercherBiensCompatibles(
                request.getLocalisation(),
                request.getBudgetMin() != null ? request.getBudgetMin() : 0f,
                request.getBudgetMax() != null ? request.getBudgetMax() : Float.MAX_VALUE,
                request.getSuperficieMin(),
                prospect
        );

        // Message selon le nombre de biens trouvés
        String message = biensRecommandes.isEmpty()
                ? "Aucun bien ne correspond exactement à vos critères. Voulez-vous élargir votre recherche ?"
                : "J'ai trouvé " + biensRecommandes.size() + " bien(s) qui correspondent à vos critères !";

        return ChatbotResponse.builder()
                .message(message)
                .biens(biensRecommandes)
                .scoreIA(prospect.getScoreIA())
                .categorieIA(prospect.getCategorieIA())
                .prochainQuestion("Ces biens vous intéressent-ils ?")
                .build();
    }

    // Enregistrer la réaction du client sur un bien
    @Transactional
    public ChatbotResponse enregistrerReaction(ChatbotRequest request) {

        Prospect prospect = prospectRepository
                .findByUserId(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Prospect introuvable"));

        // Enregistrer l'interaction
        enregistrerInteraction(prospect, request.getReaction(),
                "Bien #" + request.getBienId() + " → " + request.getReaction());

        // Mettre à jour le score IA
        mettreAJourScore(prospect, request.getReaction());

        // Message selon la réaction
        String message = switch (request.getReaction()) {
            case "LIKE" -> "Super choix ! Voulez-vous plus d'informations sur ce bien ?";
            case "DISLIKE" -> "Pas de problème ! Je vais vous proposer d'autres biens.";
            case "QUESTION" -> "Bonne question ! N'hésitez pas à contacter notre agent.";
            default -> "Merci pour votre retour !";
        };

        // Si DISLIKE → proposer de nouveaux biens
        List<BienResponse> nouveauxBiens = null;
        if ("DISLIKE".equals(request.getReaction())) {
            nouveauxBiens = rechercherBiensCompatibles(
                    prospect.getLocalisation(),
                    0f,
                    prospect.getBudget() != null ? prospect.getBudget() : Float.MAX_VALUE,
                    null,
                    prospect
            );
        }

        return ChatbotResponse.builder()
                .message(message)
                .biens(nouveauxBiens)
                .scoreIA(prospect.getScoreIA())
                .categorieIA(prospect.getCategorieIA())
                .prochainQuestion("DISLIKE".equals(request.getReaction())
                        ? "Voici d'autres suggestions !"
                        : null)
                .build();
    }

    // ======= Méthodes privées =======

    // Créer prospect automatiquement depuis le chatbot
    private Prospect creerProspectAutomatique(User client,
                                              Float budget,
                                              String localisation) {
        Prospect prospect = Prospect.builder()
                .user(client)
                .budget(budget)
                .localisation(localisation)
                .statut("NOUVEAU")
                .scoreIA(0f)
                .categorieIA("FROID")
                .dateCreation(LocalDate.now())
                .build();
        return prospectRepository.save(prospect);
    }

    // Rechercher biens compatibles en excluant ceux déjà rejetés
    private List<BienResponse> rechercherBiensCompatibles(
            String localisation,
            Float prixMin,
            Float prixMax,
            Float superficieMin,
            Prospect prospect) {

        // Récupérer les IDs des biens rejetés (DISLIKE)
        List<Long> biensRejetes = interactionRepository
                .findByProspectIdAndType(prospect.getId(), "DISLIKE")
                .stream()
                .map(i -> {
                    try {
                        String contenu = i.getContenu();
                        // Extraire l'ID du bien depuis le contenu
                        String idStr = contenu.replace("Bien #", "")
                                .split(" ")[0];
                        return Long.parseLong(idStr);
                    } catch (Exception e) {
                        return -1L;
                    }
                })
                .filter(id -> id != -1L)
                .collect(Collectors.toList());

        // Rechercher les biens disponibles
        return bienRepository.findAll()
                .stream()
                .filter(b -> b.getDisponible())
                .filter(b -> localisation == null
                        || b.getLocalisation().equalsIgnoreCase(localisation))
                .filter(b -> b.getPrix() >= prixMin && b.getPrix() <= prixMax)
                .filter(b -> superficieMin == null
                        || b.getSuperficie() >= superficieMin)
                .filter(b -> !biensRejetes.contains(b.getId()))
                .limit(5) // Maximum 5 biens
                .map(bienMapper::toBienResponse)
                .collect(Collectors.toList());
    }

    // Enregistrer une interaction
    private void enregistrerInteraction(Prospect prospect,
                                        String type,
                                        String contenu) {
        Interaction interaction = Interaction.builder()
                .type(type)
                .contenu(contenu)
                .date(LocalDate.now())
                .prospect(prospect)
                .build();
        interactionRepository.save(interaction);
    }

    // Mettre à jour le score IA
    private void mettreAJourScore(Prospect prospect, String type) {
        Float score = prospect.getScoreIA() != null
                ? prospect.getScoreIA() : 0f;

        score = switch (type) {
            case "LIKE"     -> score + 20f;
            case "DISLIKE"  -> score - 10f;
            case "QUESTION" -> score + 15f;
            case "VISITE"   -> score + 10f;
            default -> score;
        };

        score = Math.max(0f, Math.min(100f, score));
        prospect.setScoreIA(score);

        if (score >= 70)      prospect.setCategorieIA("CHAUD");
        else if (score >= 40) prospect.setCategorieIA("TIEDE");
        else                  prospect.setCategorieIA("FROID");

        prospectRepository.save(prospect);
    }
}