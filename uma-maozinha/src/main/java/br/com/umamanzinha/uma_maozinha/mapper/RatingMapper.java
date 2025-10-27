package br.com.umamanzinha.uma_maozinha.mapper;

import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesSimpleDTO;
import br.com.umamanzinha.uma_maozinha.entities.Ratings;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {
    public static Ratings toEntity(RatingRequestDTO dto) {
        Ratings rating = new Ratings();
        rating.setScore(dto.score());
        rating.setComment(dto.comment());
        return rating;
    }

    public static RatingResponseDTO toDTO(Ratings rating) {
        return new RatingResponseDTO(
            rating.getId(),
            rating.getScore(),
            rating.getComment(),
            new ServicesSimpleDTO(rating.getServices()),
            rating.getCreatedAt().toString()
        );
    }
}
