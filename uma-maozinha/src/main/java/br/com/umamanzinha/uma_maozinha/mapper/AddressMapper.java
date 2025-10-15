package br.com.umamanzinha.uma_maozinha.mapper;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.entities.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public static Address toEntity(AddressDTO dto) {
        Address address = new Address();
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setZipCode(dto.zipCode());
        address.setCountry(dto.country());
        return address;
    }

    public static AddressDTO toDto(Address address) {
        return new AddressDTO(
            address.getId(),
            address.getStreet(),
            address.getCity(),
            address.getState(),
            address.getZipCode(),
            address.getCountry()
        );
    }
}
