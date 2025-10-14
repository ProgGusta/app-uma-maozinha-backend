package br.com.umamanzinha.uma_maozinha.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddressDTO(
        Long id,
        @NotNull @NotEmpty String street,
        @NotNull @NotEmpty String city,
        @NotNull @NotEmpty String state,
        @NotNull @NotEmpty String zipCode,
        @NotNull @NotEmpty String country
) {
}
