package br.com.umamanzinha.uma_maozinha.dtos.services;

import java.math.BigDecimal;

public record ServicesWaitDTO(
        BigDecimal price,
        String description
) {
}
