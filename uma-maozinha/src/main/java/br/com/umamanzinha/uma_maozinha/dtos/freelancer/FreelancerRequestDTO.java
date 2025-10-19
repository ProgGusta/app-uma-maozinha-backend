package br.com.umamanzinha.uma_maozinha.dtos.freelancer;

import br.com.umamanzinha.uma_maozinha.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FreelancerRequestDTO(
        @NotBlank
        @Size(min=5, max=100)
        String title,

        @NotBlank
        @Size(min=20)
        String description,

        @NotNull
        CategoryType category
) {
}
