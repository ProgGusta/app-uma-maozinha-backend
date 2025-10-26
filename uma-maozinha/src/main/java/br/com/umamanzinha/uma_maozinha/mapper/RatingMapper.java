package br.com.umamanzinha.uma_maozinha.mapper;

import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingResponseDTO;
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
            ServicesMapper.toDTO(rating.getServices()), //mudar isso aqui depois, criar um serviceResumeDTO especifico para rating
            rating.getCreatedAt().toString()
        );
    }
}
