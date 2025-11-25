package br.com.umamanzinha.uma_maozinha.dtos.user;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import java.util.List;

public record UserDTO(
    Long id,
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank String password,
    @NotBlank Boolean isFreelancer,
    List<AddressDTO> addressDTO,
    List<PhoneDTO> phoneDTO
) {
}
