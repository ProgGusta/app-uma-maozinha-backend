package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import br.com.umamanzinha.uma_maozinha.dtos.UserDTO;
import br.com.umamanzinha.uma_maozinha.services.AddressService;
import br.com.umamanzinha.uma_maozinha.services.PhoneService;
import br.com.umamanzinha.uma_maozinha.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AddressService addressService;
    private final PhoneService phoneService;

    public UserController(UserService userService, AddressService addressService, PhoneService phoneService) {
        this.userService = userService;
        this.addressService = addressService;
        this.phoneService = phoneService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateById(@Valid @RequestBody UserDTO userDTO, @PathVariable Long id){
        return ResponseEntity.ok(userService.update(id,userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }

    //Address
    @PostMapping("/{userId}/address/add") //futuramente passar isso como token talvez
    public ResponseEntity<AddressDTO> addAddress(@PathVariable Long userId, @Valid @RequestBody AddressDTO addressDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddressToUser(userId,addressDTO));
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @Valid @RequestBody AddressDTO addressDTO){
        return ResponseEntity.ok(addressService.updateAddress(addressId,addressDTO));
    }

    @GetMapping("/{userId}/address/listAll")
    public ResponseEntity<List<AddressDTO>> listAllAddresses(@PathVariable Long userId){
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId));
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId){
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    // Phone
    @PostMapping("/{userId}/phone/add") // futuramente passar isso como token talvez
    public ResponseEntity<PhoneDTO> addPhone(@PathVariable Long userId, @Valid @RequestBody PhoneDTO phoneDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneService.addPhoneToUser(userId, phoneDTO));
    }

    @PutMapping("/phone/{phoneId}")
    public ResponseEntity<PhoneDTO> updatePhone(@PathVariable Long phoneId, @Valid @RequestBody PhoneDTO phoneDTO) {
        return ResponseEntity.ok(phoneService.updatePhone(phoneId, phoneDTO));
    }

    @GetMapping("/{userId}/phone/listAll")
    public ResponseEntity<List<PhoneDTO>> listAllPhones(@PathVariable Long userId) {
        return ResponseEntity.ok(phoneService.getPhonesByUserId(userId));
    }

    @DeleteMapping("/phone/{phoneId}")
    public ResponseEntity<Void> deletePhone(@PathVariable Long phoneId) {
        phoneService.deletePhone(phoneId);
        return ResponseEntity.noContent().build();
    }


}
