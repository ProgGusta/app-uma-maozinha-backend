package br.com.umamanzinha.uma_maozinha.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PhoneDTO(
    @NotNull @NotEmpty String number,
    Boolean isWhatsApp,
    String description
) {
}
