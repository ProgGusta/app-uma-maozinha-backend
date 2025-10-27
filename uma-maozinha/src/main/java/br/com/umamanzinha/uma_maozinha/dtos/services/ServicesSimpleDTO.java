package br.com.umamanzinha.uma_maozinha.dtos.services;

import br.com.umamanzinha.uma_maozinha.entities.Services;
import br.com.umamanzinha.uma_maozinha.enums.ServiceStatus;

public record ServicesSimpleDTO(
    Long id,
    ServiceStatus status,
    String description,
    String createdAt
){
    public ServicesSimpleDTO(Services services) {
        this(
            services.getId(),
            services.getStatus(),
            services.getDescription(),
            services.getCreatedAt().toString()
        );
    }
}
