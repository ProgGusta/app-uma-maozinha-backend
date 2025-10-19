package br.com.umamanzinha.uma_maozinha.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserDTO(
    Long id,
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank String password,
    List<AddressDTO> addressDTO,
    List<PhoneDTO> phoneDTO
) {
}
