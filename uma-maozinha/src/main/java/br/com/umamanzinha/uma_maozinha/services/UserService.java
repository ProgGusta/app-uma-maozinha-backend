package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.user.UserDTO;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.exceptions.BusinessRuleException;
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
    private final AddressService addressService;
    private final PhoneService phoneService;

    public UserService(UserRepository userRepository, AddressRepository addressRepository, PhoneRepository phoneRepository, AddressService addressService, PhoneService phoneService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
        this.addressService = addressService;
        this.phoneService = phoneService;
    }


    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new BusinessRuleException("Email already in use"); // TODO: criar exception especifica
        }

        User user  = UserMapper.toEntity(userDTO);
        user = userRepository.save(user);

        if (userDTO.addressDTO() != null)
            addressService.saveAllAddresses(user, userDTO.addressDTO());

        if (userDTO.phoneDTO() != null)
            phoneService.saveAllPhones(user, userDTO.phoneDTO());



        return UserMapper.toDto(user);
    }

    @Transactional
    public UserDTO getById(Long id) {
        return UserMapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found")));
    }
    @Transactional
    public List<UserDTO> getAll(){
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }


    public UserDTO update(Long id,UserDTO userDTO){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userRepository.existsByEmail(userDTO.email())) {
            throw new BusinessRuleException("Email already in use"); // TODO: criar exception especifica
        }

        user.setEmail(userDTO.email());
        user.setName(userDTO.name());
        user.setPassword(userDTO.password());


        User userSaved = userRepository.save(user);

        return UserMapper.toDto(userSaved);

    }

    public void deleteById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);
    }
}
