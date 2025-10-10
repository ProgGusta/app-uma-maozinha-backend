package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.UserDTO;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.mapper.UserMapper;
import br.com.umamanzinha.uma_maozinha.repository.AddressRepository;
import br.com.umamanzinha.uma_maozinha.repository.PhoneService;
import br.com.umamanzinha.uma_maozinha.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PhoneService phoneService;

    public UserService(UserRepository userRepository, AddressRepository addressRepository, PhoneService phoneService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.phoneService = phoneService;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user  = UserMapper.toEntity(userDTO);


        user = userRepository.save(user);

        userDTO = UserMapper.toDto(user);
        return userDTO;
    }
}
