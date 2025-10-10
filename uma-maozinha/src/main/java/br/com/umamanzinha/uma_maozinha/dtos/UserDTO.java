package br.com.umamanzinha.uma_maozinha.dtos;

import java.util.List;

public record UserDTO(
    Long id,
    String name,
    String email,
    String password,
    List<AddressDTO> addressDTO,
    List<PhoneDTO> phoneDTO
) {
}
