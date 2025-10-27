package br.com.umamanzinha.uma_maozinha.mapper;

import br.com.umamanzinha.uma_maozinha.dtos.user.UserDTO;
import br.com.umamanzinha.uma_maozinha.entities.User;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public static User toEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        return user;
    }

    public static UserDTO toDto(User user){
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getAddresses()
                        .stream()
                        .map(AddressMapper::toDto)
                        .collect(Collectors.toList()),
            user.getPhones()
                        .stream()
                        .map(PhoneMapper::toDto)
                        .collect(Collectors.toList())
        );
    }


}
