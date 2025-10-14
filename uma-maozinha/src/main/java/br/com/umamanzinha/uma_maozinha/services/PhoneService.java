package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import br.com.umamanzinha.uma_maozinha.entities.Address;
import br.com.umamanzinha.uma_maozinha.entities.Phone;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.exceptions.ResourceNotFoundException;
import br.com.umamanzinha.uma_maozinha.mapper.AddressMapper;
import br.com.umamanzinha.uma_maozinha.mapper.PhoneMapper;
import br.com.umamanzinha.uma_maozinha.repository.PhoneRepository;
import br.com.umamanzinha.uma_maozinha.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneService {
    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;


    public PhoneService(PhoneRepository phoneRepository, UserRepository userRepository) {
        this.phoneRepository = phoneRepository;
        this.userRepository = userRepository;
    }

    public void saveAllPhones(User user, List<PhoneDTO> phoneDTOs) {
        List<Phone> phones = phoneDTOs.stream()
                .map(PhoneMapper::toEntity)
                .peek(address -> address.setUser(user)) // salva a referência do usuário em cada telefone senão dá erro de chave estrangeira
                .toList();

        phoneRepository.saveAll(phones);
        user.setPhones(phones);
    }

    public PhoneDTO addPhoneToUser(Long userId, PhoneDTO phoneDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Phone phone = PhoneMapper.toEntity(phoneDTO);
        phone.setUser(user);
        Phone savedPhone = phoneRepository.save(phone);
        return PhoneMapper.toDto(savedPhone);
    }

    public PhoneDTO updatePhone(Long phoneId, PhoneDTO phoneDTO) {
        Phone phone = phoneRepository.findById(phoneId).orElseThrow(
                () -> new ResourceNotFoundException("Phone not found")
        );

        phone.setNumber(phoneDTO.number());
        phone.setDescription(phoneDTO.description());
        phone.setIsWhatsApp(phoneDTO.isWhatsApp());

        Phone updatedPhone = phoneRepository.save(phone);
        return PhoneMapper.toDto(updatedPhone);
    }

    public void deletePhone(Long phoneId) {
        Phone phone = phoneRepository.findById(phoneId).orElseThrow(
                () -> new ResourceNotFoundException("Phone not found")
        );
        phoneRepository.delete(phone);
    }

    public List<PhoneDTO> getPhonesByUserId(Long userId) {
        List<Phone> phones = phoneRepository.findByUserId(userId);
        return phones.stream()
                .map(PhoneMapper::toDto)
                .toList();
    }
}
