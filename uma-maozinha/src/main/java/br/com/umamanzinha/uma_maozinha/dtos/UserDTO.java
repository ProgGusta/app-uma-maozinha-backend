package br.com.umamanzinha.uma_maozinha.dtos;

import java.util.List;

public record UserDTO(
    String name,
    String email,
    String password,
    List<AddressDTO> addressDTO,
    List<PhoneDTO> phoneDTO
) {
}
