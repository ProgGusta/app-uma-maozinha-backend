package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingResponseDTO;
import br.com.umamanzinha.uma_maozinha.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service/{serviceId}/rating")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping()
    public ResponseEntity<RatingResponseDTO> createRating(@Valid @RequestBody RatingRequestDTO ratingRequestDTO, @PathVariable Long serviceId, @AuthenticationPrincipal JwtUserData data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.createRating(ratingRequestDTO, serviceId, data.id()));
    }

    @PatchMapping("/{ratingId}")
    public ResponseEntity<RatingResponseDTO> updateRating(@PathVariable Long ratingId,@Valid @RequestBody RatingRequestDTO ratingRequestDTO, @AuthenticationPrincipal JwtUserData data) {
        return ResponseEntity.ok(ratingService.updateRating(ratingId, ratingRequestDTO, data.id()));
    }
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId, @AuthenticationPrincipal JwtUserData data) {
        ratingService.deleteRating(ratingId, data.id());
        return ResponseEntity.noContent().build();

    }

}
