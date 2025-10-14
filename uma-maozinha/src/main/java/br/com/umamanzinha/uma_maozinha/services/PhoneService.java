package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import br.com.umamanzinha.uma_maozinha.entities.Address;
import br.com.umamanzinha.uma_maozinha.entities.Phone;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.mapper.AddressMapper;
import br.com.umamanzinha.uma_maozinha.mapper.PhoneMapper;
import br.com.umamanzinha.uma_maozinha.repository.PhoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneService {
    private final PhoneRepository phoneRepository;

    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    public void saveAllPhones(User user, List<PhoneDTO> phoneDTOs) {
        List<Phone> phones = phoneDTOs.stream()
                .map(PhoneMapper::toEntity)
                .peek(address -> address.setUser(user)) // salva a referência do usuário em cada telefone senão dá erro de chave estrangeira
                .toList();

        phoneRepository.saveAll(phones);
        user.setPhones(phones);
    }
}
