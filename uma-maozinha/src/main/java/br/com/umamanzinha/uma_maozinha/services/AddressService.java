package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.entities.Address;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.mapper.AddressMapper;
import br.com.umamanzinha.uma_maozinha.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void saveAllAddresses(User user, List<AddressDTO> addressDTOs) {
        List<Address> addreesses = addressDTOs.stream()
                .map(AddressMapper::toEntity)
                .peek(address -> address.setUser(user)) // salva a referência do usuário em cada endereço senão dá erro de chave estrangeira
                .toList();

        addressRepository.saveAll(addreesses);
        user.setAddresses(addreesses);
    }
}
