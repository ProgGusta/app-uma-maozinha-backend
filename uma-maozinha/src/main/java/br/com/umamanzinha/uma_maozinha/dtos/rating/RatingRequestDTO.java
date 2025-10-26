package br.com.umamanzinha.uma_maozinha.dtos.rating;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RatingRequestDTO(
    @NotNull Double score,
    String comment
) {
}
