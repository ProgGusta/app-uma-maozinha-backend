package br.com.umamanzinha.uma_maozinha.dtos.services;

import java.math.BigDecimal;

public record ServicesRequestDTO(
        BigDecimal price, // pensar melhor aqui
        String location,
        String description,
        String createdAt,
        Long userId // tirar quando começar a usar tokens
) {
}
