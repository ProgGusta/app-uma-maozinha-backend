package br.com.umamanzinha.uma_maozinha.mapper;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import br.com.umamanzinha.uma_maozinha.dtos.UserDTO;
import br.com.umamanzinha.uma_maozinha.entities.Address;
import br.com.umamanzinha.uma_maozinha.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        List<AddressDTO> addresses = dto.addressDTO();
        user.setAddresses(addresses.stream()
                .map(AddressMapper::toEntity)
                .collect(Collectors.toList()));
        user.getAddresses().forEach(address -> address.setUser(user)); // salva a referência do usuário em cada endereço senão dá erro de chave estrangeira

        List<PhoneDTO> phones = dto.phoneDTO();
        user.setPhones((phones.stream()
                .map(PhoneMapper::toEntity)
                .collect(Collectors.toList())));
        user.getPhones().forEach(phone -> phone.setUser(user)); // salva a referência do usuário em cada telefone senão dá erro de chave estrangeira
        return user;
    }

    public static UserDTO toDto(User user){
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getAddresses().stream()
                    .map(AddressMapper::toDto)
                    .collect(Collectors.toList()),
            user.getPhones().stream()
                    .map(PhoneMapper::toDto)
                    .collect(Collectors.toList())
        );
    }


}
