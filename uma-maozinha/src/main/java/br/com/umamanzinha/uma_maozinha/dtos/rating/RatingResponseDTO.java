package br.com.umamanzinha.uma_maozinha.dtos.rating;

import br.com.umamanzinha.uma_maozinha.dtos.ServicesDTO;

public record RatingResponseDTO (
    Long id,
    Double score,
    String comment,
    ServicesDTO servicesDTO,
    String createdAt
){
}

