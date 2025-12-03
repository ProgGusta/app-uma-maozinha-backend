package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.freelancer.FreelancerRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.freelancer.FreelancerResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingResponseDTO;
import br.com.umamanzinha.uma_maozinha.services.FreelancerProfileService;
import br.com.umamanzinha.uma_maozinha.services.RatingService;
import br.com.umamanzinha.uma_maozinha.services.ServicesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/freelancers")
@Tag(name = "Perfis de Freelancer", description = "Operações para gerenciamento de perfis de freelancer, serviços e avaliações.")
public class FreelancerProfileController {

    private final FreelancerProfileService freelancerProfileService;
    private final ServicesService servicesService;
    private final RatingService ratingService;

    @Autowired
    public FreelancerProfileController(FreelancerProfileService freelancerProfileService, ServicesService servicesService, RatingService ratingService) {
        this.freelancerProfileService = freelancerProfileService;
        this.servicesService = servicesService;
        this.ratingService = ratingService;
    }

    @Operation(summary = "Criar perfil de freelancer", description = "Cria um novo perfil de freelancer para o usuário autenticado. Apenas o próprio usuário pode criar seu perfil.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso", content = @Content(schema = @Schema(implementation = FreelancerResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode criar perfil para outro usuário", content = @Content)
    })
    @PostMapping("/{user_id}/create")
    public ResponseEntity<FreelancerResponseDTO> create(
            @Parameter(description = "ID do usuário", required = true, example = "1") @PathVariable Long user_id,
            @Parameter(description = "Dados do perfil de freelancer", required = true) @Valid @RequestBody FreelancerRequestDTO freelancerRequestDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(freelancerProfileService.createFreelancer(user_id, freelancerRequestDTO, data.id()));
    }

    @Operation(summary = "Buscar perfil de freelancer por ID", description = "Retorna os dados de um perfil de freelancer pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil encontrado", content = @Content(schema = @Schema(implementation = FreelancerResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado", content = @Content)
    })
    @GetMapping("/{profile_id}")
    public ResponseEntity<FreelancerResponseDTO> getProfileById(
            @Parameter(description = "ID do perfil de freelancer", required = true, example = "10") @PathVariable Long profile_id){
        FreelancerResponseDTO profileDTO = freelancerProfileService.getProfileById(profile_id);
        return ResponseEntity.ok(profileDTO);
    }

    @Operation(summary = "Listar perfis de freelancer por usuário", description = "Retorna todos os perfis de freelancer associados ao usuário informado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfis encontrados", content = @Content(schema = @Schema(implementation = FreelancerResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<FreelancerResponseDTO>> getProfilesByUserId(
            @Parameter(description = "ID do usuário", required = true, example = "1") @PathVariable Long user_id) {
        List<FreelancerResponseDTO> profiles = freelancerProfileService.getProfilesByUserId(user_id);
        return ResponseEntity.ok(profiles);
    }

    @Operation(summary = "Atualizar perfil de freelancer", description = "Atualiza os dados do perfil de freelancer. Apenas o dono do perfil pode realizar a atualização.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso", content = @Content(schema = @Schema(implementation = FreelancerResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode atualizar perfil de outro usuário", content = @Content),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado", content = @Content)
    })
    @PutMapping("/{profile_id}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerResponseDTO> updateFreelancer(
            @Parameter(description = "ID do perfil de freelancer", required = true, example = "10") @PathVariable Long profile_id,
            @Parameter(description = "Dados do perfil de freelancer", required = true) @Valid @RequestBody FreelancerRequestDTO dto,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        FreelancerResponseDTO updatedProfile = freelancerProfileService.updateProfile(profile_id, dto, data.id());
        return ResponseEntity.ok(updatedProfile);
    }

    @Operation(summary = "Excluir perfil de freelancer", description = "Remove o perfil de freelancer. Apenas o dono do perfil pode realizar a exclusão.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Perfil excluído com sucesso", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode excluir perfil de outro usuário", content = @Content),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado", content = @Content)
    })
    @DeleteMapping("/{profile_id}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Void>  deleteProfile(
            @Parameter(description = "ID do perfil de freelancer", required = true, example = "10") @PathVariable Long profile_id,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {
        freelancerProfileService.deleteProfile(profile_id, data.id());

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar serviços do freelancer", description = "Retorna todos os serviços cadastrados pelo freelancer informado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Serviços encontrados", content = @Content(schema = @Schema(implementation = ServicesResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    @GetMapping("/{freelancerId}/services")
    public ResponseEntity<List<ServicesResponseDTO>> getServicesByFreelancer(
            @Parameter(description = "ID do freelancer", required = true, example = "10") @PathVariable Long freelancerId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {
        List<ServicesResponseDTO> services = servicesService.getAllServicesByFreelancerId(freelancerId, data.id());
        return ResponseEntity.ok(services);
    }

    @Operation(summary = "Listar avaliações do freelancer", description = "Retorna todas as avaliações recebidas pelo freelancer informado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliações encontradas", content = @Content(schema = @Schema(implementation = RatingResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    @GetMapping("/{freelancerId}/ratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatingsByFreelancer(
            @Parameter(description = "ID do freelancer", required = true, example = "10") @PathVariable Long freelancerId) {
        List<RatingResponseDTO> ratings = ratingService.getAllRatingsByFreelancerProfileId(freelancerId);
        return ResponseEntity.ok(ratings);
    }
}
