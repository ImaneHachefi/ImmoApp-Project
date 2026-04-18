package main.immoapp.repository;

import main.immoapp.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByBienIdOrderByOrdreAsc(Long bienId);
}