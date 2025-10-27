package br.com.umamanzinha.uma_maozinha.dtos.services;

import br.com.umamanzinha.uma_maozinha.dtos.freelancer.FreelancerSimpleResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.user.UserSimpleResponseDTO;
import br.com.umamanzinha.uma_maozinha.enums.ServiceStatus;

import java.math.BigDecimal;

public record ServicesResponseDTO(
        Long id,
        ServiceStatus status,
        BigDecimal price,
        String location,
        String description,
        String createdAt,
        FreelancerSimpleResponseDTO freelancer,
        UserSimpleResponseDTO user
) {
}
