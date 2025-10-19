package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.dtos.freelancer.FreelancerRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.freelancer.FreelancerResponseDTO;
import br.com.umamanzinha.uma_maozinha.services.FreelancerProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/freelancers")
public class FreelancerProfileController {

    private final FreelancerProfileService freelancerProfileService;

    @Autowired
    public FreelancerProfileController(FreelancerProfileService freelancerProfileService) {
        this.freelancerProfileService = freelancerProfileService;
    }

    @PostMapping("/{user_id}/create")
    public ResponseEntity<FreelancerResponseDTO> create(@PathVariable Long user_id,
                                                        @Valid @RequestBody FreelancerRequestDTO freelancerRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(freelancerProfileService.createFreelancer(user_id, freelancerRequestDTO));
    }

    @GetMapping("/{profile_id}")
    public ResponseEntity<FreelancerResponseDTO> getProfileById(@PathVariable Long profile_id){
        FreelancerResponseDTO profileDTO = freelancerProfileService.getProfileById(profile_id);
        return ResponseEntity.ok(profileDTO);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<FreelancerResponseDTO>> getProfilesByUserId(@PathVariable Long user_id) {
        List<FreelancerResponseDTO> profiles = freelancerProfileService.getProfilesByUserId(user_id);
        return ResponseEntity.ok(profiles);
    }

    @PutMapping("/{profile_id}")
    public ResponseEntity<FreelancerResponseDTO> updateFreelancer(
            @PathVariable Long profile_id,
            @Valid @RequestBody FreelancerRequestDTO dto) {

        FreelancerResponseDTO updatedProfile = freelancerProfileService.updateProfile(profile_id, dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{profile_id}")
    public ResponseEntity<Void>  deleteProfile(@PathVariable Long profile_id){
        freelancerProfileService.deleteProfile(profile_id);

        return ResponseEntity.noContent().build();
    }
}
