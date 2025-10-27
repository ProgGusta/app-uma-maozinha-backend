package br.com.umamanzinha.uma_maozinha.dtos.rating;

import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesSimpleDTO;

public record RatingResponseDTO (
    Long id,
    Double score,
    String comment,
    ServicesSimpleDTO servicesDTO,
    String createdAt
){
}

