package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public ResponseEntity<AddressDTO> addAddress(@PathVariable Long userId, @Valid @RequestBody AddressDTO addressDTO, @AuthenticationPrincipal JwtUserData data){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddressToUser(userId,addressDTO, data.id()));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @Valid @RequestBody AddressDTO addressDTO, @AuthenticationPrincipal JwtUserData data){
        return ResponseEntity.ok(addressService.updateAddress(addressId,addressDTO, data.id()));
    }

    @GetMapping("/{userId}/listAll")
    public ResponseEntity<List<AddressDTO>> listAllAddresses(@PathVariable Long userId,@AuthenticationPrincipal JwtUserData data){
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId, data.id()));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId, @AuthenticationPrincipal JwtUserData data){
        addressService.deleteAddress(addressId,data.id());
        return ResponseEntity.noContent().build();
    }
}
