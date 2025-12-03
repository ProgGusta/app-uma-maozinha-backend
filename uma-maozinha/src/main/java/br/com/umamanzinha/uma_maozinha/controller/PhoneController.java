package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import br.com.umamanzinha.uma_maozinha.exceptions.ForbiddenException;
import br.com.umamanzinha.uma_maozinha.services.PhoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone")
public class PhoneController {
    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<PhoneDTO> addPhone(
            @PathVariable Long userId,
            @Valid @RequestBody PhoneDTO phoneDTO,
            @AuthenticationPrincipal JwtUserData data) {

        if (!data.id().equals(userId)) {
            throw new ForbiddenException("You cannot add a phone to another user!");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(phoneService.addPhoneToUser(userId, phoneDTO));
    }

    @PutMapping("/{phoneId}")
    public ResponseEntity<PhoneDTO> updatePhone(
            @PathVariable Long phoneId,
            @Valid @RequestBody PhoneDTO phoneDTO,
            @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(phoneService.updatePhone(phoneId, phoneDTO, data.id()));
    }

    @GetMapping("/{userId}/listAll")
    public ResponseEntity<List<PhoneDTO>> listAllPhones(
            @PathVariable Long userId,
            @AuthenticationPrincipal JwtUserData data) {

        if (!data.id().equals(userId)) {
            throw new ForbiddenException("You cannot access phones from another user!");
        }

        return ResponseEntity.ok(phoneService.getPhonesByUserId(userId));
    }

    @DeleteMapping("/{phoneId}")
    public ResponseEntity<Void> deletePhone(
            @PathVariable Long phoneId,
            @AuthenticationPrincipal JwtUserData data) {

        phoneService.deletePhone(phoneId, data.id());
        return ResponseEntity.noContent().build();
    }
}
