package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adress")
public class AdressController {
    private final AddressService addressService;

    public AdressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/{userId}/add") //futuramente passar isso como token talvez
    public ResponseEntity<AddressDTO> addAddress(@PathVariable Long userId, @Valid @RequestBody AddressDTO addressDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddressToUser(userId,addressDTO));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @Valid @RequestBody AddressDTO addressDTO){
        return ResponseEntity.ok(addressService.updateAddress(addressId,addressDTO));
    }

    @GetMapping("/{userId}/listAll")
    public ResponseEntity<List<AddressDTO>> listAllAddresses(@PathVariable Long userId){
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId){
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
