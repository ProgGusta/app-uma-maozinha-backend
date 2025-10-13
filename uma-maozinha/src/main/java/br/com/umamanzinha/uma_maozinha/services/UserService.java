package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.UserDTO;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.exceptions.ResourceNotFoundException;
import br.com.umamanzinha.uma_maozinha.mapper.UserMapper;
import br.com.umamanzinha.uma_maozinha.repository.AddressRepository;
import br.com.umamanzinha.uma_maozinha.repository.PhoneRepository;
import br.com.umamanzinha.uma_maozinha.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;

    public UserService(UserRepository userRepository, AddressRepository addressRepository, PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user  = UserMapper.toEntity(userDTO);
        user = userRepository.save(user);

        userDTO = UserMapper.toDto(user);
        return userDTO;
    }

    @Transactional
    public UserDTO getById(Long id) {
        return UserMapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")));
    }

    public List<UserDTO> getAll(){
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
