package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingResponseDTO;
import br.com.umamanzinha.uma_maozinha.services.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service/{serviceId}/rating")
@Tag(name = "Avaliações de Serviços", description = "Endpoints para criar, atualizar e excluir avaliações de serviços. Regras de negócio: só é possível avaliar serviços concluídos, cada serviço só pode ter uma avaliação, e apenas o dono do serviço pode avaliar.")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Operation(summary = "Criar avaliação para um serviço", description = "Cria uma avaliação para o serviço informado. Só é possível avaliar serviços concluídos, cada serviço só pode ter uma avaliação, e apenas o dono do serviço pode avaliar.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso", content = @Content(schema = @Schema(implementation = RatingResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Regras de negócio violadas (serviço não concluído, avaliação já existente)", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode avaliar serviço de outro usuário", content = @Content),
        @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    })
    @PostMapping()
    public ResponseEntity<RatingResponseDTO> createRating(
            @Parameter(description = "Dados da avaliação", required = true) @Valid @RequestBody RatingRequestDTO ratingRequestDTO,
            @Parameter(description = "ID do serviço", required = true, example = "1") @PathVariable Long serviceId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.createRating(ratingRequestDTO, serviceId, data.id()));
    }

    @Operation(summary = "Atualizar avaliação de um serviço", description = "Atualiza os dados de uma avaliação existente. Apenas o dono do serviço pode atualizar sua avaliação.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso", content = @Content(schema = @Schema(implementation = RatingResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regras de negócio violadas", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode atualizar avaliação de outro usuário", content = @Content),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @PatchMapping("/{ratingId}")
    public ResponseEntity<RatingResponseDTO> updateRating(
            @Parameter(description = "ID da avaliação", required = true, example = "10") @PathVariable Long ratingId,
            @Parameter(description = "Dados da avaliação", required = true) @Valid @RequestBody RatingRequestDTO ratingRequestDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {
        return ResponseEntity.ok(ratingService.updateRating(ratingId, ratingRequestDTO, data.id()));
    }

    @Operation(summary = "Excluir avaliação de um serviço", description = "Remove uma avaliação existente. Apenas o dono do serviço pode excluir sua avaliação.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Avaliação excluída com sucesso", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode excluir avaliação de outro usuário", content = @Content),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content)
    })
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(
            @Parameter(description = "ID da avaliação", required = true, example = "10") @PathVariable Long ratingId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {
        ratingService.deleteRating(ratingId, data.id());
        return ResponseEntity.noContent().build();
    }

}
