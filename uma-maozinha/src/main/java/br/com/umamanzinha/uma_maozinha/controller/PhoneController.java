package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import br.com.umamanzinha.uma_maozinha.services.PhoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone")
public class PhoneController {
    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PostMapping("/{userId}/add") // futuramente passar isso como token talvez
    public ResponseEntity<PhoneDTO> addPhone(@PathVariable Long userId, @Valid @RequestBody PhoneDTO phoneDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(phoneService.addPhoneToUser(userId, phoneDTO));
    }

    @PutMapping("/{phoneId}")
    public ResponseEntity<PhoneDTO> updatePhone(@PathVariable Long phoneId, @Valid @RequestBody PhoneDTO phoneDTO) {
        return ResponseEntity.ok(phoneService.updatePhone(phoneId, phoneDTO));
    }

    @GetMapping("/{userId}/listAll")
    public ResponseEntity<List<PhoneDTO>> listAllPhones(@PathVariable Long userId) {
        return ResponseEntity.ok(phoneService.getPhonesByUserId(userId));
    }

    @DeleteMapping("/{phoneId}")
    public ResponseEntity<Void> deletePhone(@PathVariable Long phoneId) {
        phoneService.deletePhone(phoneId);
        return ResponseEntity.noContent().build();
    }
}
