package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.entities.Address;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.exceptions.ResourceNotFoundException;
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

    public AddressDTO addAddressToUser(Long userId, AddressDTO addressDTO) {
        Address address = AddressMapper.toEntity(addressDTO);
        address.getUser().setId(userId);
        Address savedAddress = addressRepository.save(address);
        return AddressMapper.toDto(savedAddress);
    }

    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO){

        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found")
        );

        address.setStreet(addressDTO.street());
        address.setCity(addressDTO.city());
        address.setState(addressDTO.state());
        address.setZipCode(addressDTO.zipCode());
        address.setCountry(addressDTO.country());

        Address updatedAddress = addressRepository.save(address);
        return AddressMapper.toDto(updatedAddress);
    }

    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address not found")
        );
        addressRepository.delete(address);
    }

    public List<AddressDTO> getAddressesByUserId(Long userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream()
                .map(AddressMapper::toDto)
                .toList();
    }
    //futuramente criar um metodo de validação de CEP com API externa
}
