package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import br.com.umamanzinha.uma_maozinha.dtos.ServicesDTO;
import br.com.umamanzinha.uma_maozinha.dtos.UserDTO;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingResponseDTO;
import br.com.umamanzinha.uma_maozinha.services.*;
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
    private final ServicesService servicesService;
    private final RatingService ratingService;

    public UserController(UserService userService, AddressService addressService, PhoneService phoneService, ServicesService servicesService, RatingService ratingService) {
        this.userService = userService;
        this.addressService = addressService;
        this.phoneService = phoneService;
        this.servicesService = servicesService;
        this.ratingService = ratingService;
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

    // services
    @GetMapping("{userId}/user")
    public ResponseEntity<List<ServicesDTO>> getServicesByUser(@PathVariable Long userId) {
        List<ServicesDTO> services = servicesService.getAllServicesByUserId(userId);
        return ResponseEntity.ok(services);
    }

    //ratings
    @GetMapping("/{userId}/ratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatingsByUser(@PathVariable Long userId) {
        List<RatingResponseDTO> ratings = ratingService.getAllRatingsByUserId(userId);
        return ResponseEntity.ok(ratings);
    }
}
