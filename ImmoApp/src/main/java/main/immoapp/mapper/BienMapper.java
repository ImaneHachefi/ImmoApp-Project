package main.immoapp.mapper;

import main.immoapp.dto.response.BienResponse;
import main.immoapp.dto.response.PhotoResponse;
import main.immoapp.entity.BienImmobilier;
import main.immoapp.entity.Photo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BienMapper {

    BienResponse toBienResponse(BienImmobilier bien);

    PhotoResponse toPhotoResponse(Photo photo);
}