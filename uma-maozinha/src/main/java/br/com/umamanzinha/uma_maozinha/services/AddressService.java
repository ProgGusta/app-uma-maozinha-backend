package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.entities.Address;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.exceptions.ForbiddenException;
import br.com.umamanzinha.uma_maozinha.exceptions.ResourceNotFoundException;
import br.com.umamanzinha.uma_maozinha.mapper.AddressMapper;
import br.com.umamanzinha.uma_maozinha.repository.AddressRepository;
import br.com.umamanzinha.uma_maozinha.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public void saveAllAddresses(User user, List<AddressDTO> addressDTOs) {
        List<Address> addreesses = addressDTOs.stream()
                .map(AddressMapper::toEntity)
                .peek(address -> address.setUser(user)) // salva a referência do usuário em cada endereço senão dá erro de chave estrangeira
                .toList();

        addressRepository.saveAll(addreesses);
        user.setAddresses(addreesses);
    }

    @Transactional
    public AddressDTO addAddressToUser(Long userId, AddressDTO addressDTO, Long authId) {
        if (!userId.equals(authId)) {
            throw new ForbiddenException("You are not authorized to add an address for this user.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = AddressMapper.toEntity(addressDTO);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return AddressMapper.toDto(savedAddress);
    }

    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO, Long authId){

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found")
        );

        if (!address.getUser().getId().equals(authId)) {
            throw new ForbiddenException("You are not authorized to update this address.");
        }

        address.setStreet(addressDTO.street());
        address.setCity(addressDTO.city());
        address.setState(addressDTO.state());
        address.setZipCode(addressDTO.zipCode());
        address.setCountry(addressDTO.country());

        Address updatedAddress = addressRepository.save(address);
        return AddressMapper.toDto(updatedAddress);
    }

    public void deleteAddress(Long addressId, Long authId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found")
        );
        if (!address.getUser().getId().equals(authId)) {
            throw new ForbiddenException("You are not authorized to delete this address.");
        }
        addressRepository.delete(address);
    }

    public List<AddressDTO> getAddressesByUserId(Long userId, Long authId) {
        if (!userId.equals(authId)) {
            throw new ForbiddenException("You are not authorized to view addresses for this user.");
        }
        if (!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found");
        }
        List<Address> addresses = addressRepository.findByUserId(userId);

        return addresses.stream()
                .map(AddressMapper::toDto)
                .toList();
    }
    //TODO: futuramente criar um metodo de validação de CEP com API externa - kkkkkkkkkkkkkkkkkkkkkkkkkkkkk

}
