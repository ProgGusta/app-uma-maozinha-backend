package br.com.umamanzinha.uma_maozinha.dtos;

import br.com.umamanzinha.uma_maozinha.enums.ServiceStatus;

import java.math.BigDecimal;

public record ServicesDTO(
    Long id,
    ServiceStatus status,
    BigDecimal price,
    String location,
    String description,
    String createdAt,
    Long freelancerProfileId, //TODO: por dto personalizado
    Long userId //TODO: por dto personalizado
) {
}
