package br.com.umamanzinha.uma_maozinha.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserDTO(
    Long id,
    @NotBlank @NotEmpty @NotNull String name,
    @NotBlank @NotEmpty @NotNull String email,
    @NotBlank @NotEmpty @NotNull String password,
    List<AddressDTO> addressDTO,
    List<PhoneDTO> phoneDTO
) {
}
