package main.immoapp.service.impl;

import main.immoapp.dto.request.BienRequest;
import main.immoapp.dto.response.BienResponse;
import main.immoapp.entity.BienImmobilier;
import main.immoapp.entity.Photo;
import main.immoapp.exception.ResourceNotFoundException;
import main.immoapp.mapper.BienMapper;
import main.immoapp.repository.BienRepository;
import main.immoapp.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BienService {

    private final BienRepository bienRepository;
    private final PhotoRepository photoRepository;
    private final BienMapper bienMapper;

    // Créer un bien
    @Transactional
    public BienResponse creerBien(BienRequest request) {
        BienImmobilier bien = BienImmobilier.builder()
                .titre(request.getTitre())
                .description(request.getDescription())
                .prix(request.getPrix())
                .disponible(request.getDisponible())
                .localisation(request.getLocalisation())
                .superficie(request.getSuperficie())
                .dateAjout(LocalDate.now())
                .build();

        bienRepository.save(bien);

        // Ajout des photos
        if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
            List<Photo> photos = new ArrayList<>();
            for (int i = 0; i < request.getPhotos().size(); i++) {
                Photo photo = Photo.builder()
                        .url(request.getPhotos().get(i))
                        .ordre(i + 1)
                        .bien(bien)
                        .build();
                photos.add(photo);
            }
            photoRepository.saveAll(photos);
            bien.setPhotos(photos);
        }

        return bienMapper.toBienResponse(bien);
    }

    // Lister tous les biens
    @Transactional(readOnly = true)
    public List<BienResponse> listerBiens() {
        return bienRepository.findAll()
                .stream()
                .map(bienMapper::toBienResponse)
                .collect(Collectors.toList());
    }

    // Lister biens disponibles
    @Transactional(readOnly = true)
    public List<BienResponse> listerBiensDisponibles() {
        return bienRepository.findByDisponibleTrue()
                .stream()
                .map(bienMapper::toBienResponse)
                .collect(Collectors.toList());
    }

    // Obtenir un bien par ID
    @Transactional(readOnly = true)
    public BienResponse getBienById(Long id) {
        BienImmobilier bien = bienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bien introuvable avec l'id : " + id));
        return bienMapper.toBienResponse(bien);
    }

    // Modifier un bien
    @Transactional
    public BienResponse modifierBien(Long id, BienRequest request) {
        BienImmobilier bien = bienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bien introuvable avec l'id : " + id));

        bien.setTitre(request.getTitre());
        bien.setDescription(request.getDescription());
        bien.setPrix(request.getPrix());
        bien.setDisponible(request.getDisponible());
        bien.setLocalisation(request.getLocalisation());
        bien.setSuperficie(request.getSuperficie());

        // Mise à jour des photos
        if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
            photoRepository.deleteAll(bien.getPhotos());
            List<Photo> photos = new ArrayList<>();
            for (int i = 0; i < request.getPhotos().size(); i++) {
                Photo photo = Photo.builder()
                        .url(request.getPhotos().get(i))
                        .ordre(i + 1)
                        .bien(bien)
                        .build();
                photos.add(photo);
            }
            photoRepository.saveAll(photos);
            bien.setPhotos(photos);
        }

        bienRepository.save(bien);
        return bienMapper.toBienResponse(bien);
    }

    // Supprimer un bien
    @Transactional
    public void supprimerBien(Long id) {
        BienImmobilier bien = bienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bien introuvable avec l'id : " + id));
        bienRepository.delete(bien);
    }

    // Rechercher biens par localisation et prix
    @Transactional(readOnly = true)
    public List<BienResponse> rechercherBiens(String localisation,
                                              Float prixMin,
                                              Float prixMax) {
        return bienRepository
                .findByLocalisationAndPrixBetweenAndDisponibleTrue(
                        localisation, prixMin, prixMax)
                .stream()
                .map(bienMapper::toBienResponse)
                .collect(Collectors.toList());
    }
}