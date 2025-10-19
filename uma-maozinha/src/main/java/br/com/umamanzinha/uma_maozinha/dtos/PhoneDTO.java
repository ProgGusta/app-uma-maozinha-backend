package br.com.umamanzinha.uma_maozinha.dtos;

import jakarta.validation.constraints.NotBlank;

public record PhoneDTO(
    Long id,
    @NotBlank String number,
    Boolean isWhatsApp,
    String description
) {
}
