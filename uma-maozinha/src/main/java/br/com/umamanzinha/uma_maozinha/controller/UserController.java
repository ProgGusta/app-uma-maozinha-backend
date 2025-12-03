package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.user.UserDTO;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingResponseDTO;
import br.com.umamanzinha.uma_maozinha.services.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Endpoints para cadastro, consulta, atualização e exclusão de usuários. Regras de negócio: e-mail único, apenas o próprio usuário pode atualizar ou excluir seus dados.")
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

    @Operation(summary = "Criar usuário", description = "Cria um novo usuário. O e-mail deve ser único. Endereços e telefones podem ser informados no cadastro.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "E-mail já em uso ou dados inválidos", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@Parameter(description = "Dados do usuário", required = true) @Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@Parameter(description = "ID do usuário", required = true, example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(summary = "Listar todos os usuários", description = "Retorna todos os usuários cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    })
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados do usuário. Apenas o próprio usuário pode atualizar seus dados. O e-mail deve ser único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "E-mail já em uso ou dados inválidos", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode atualizar dados de outro usuário", content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateById(
            @Parameter(description = "Dados do usuário", required = true) @Valid @RequestBody UserDTO userDTO,
            @Parameter(description = "ID do usuário", required = true, example = "1") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data){
        return ResponseEntity.ok(userService.update(id,userDTO, data.id()));
    }

    @Operation(summary = "Excluir usuário", description = "Remove o usuário do sistema. Apenas o próprio usuário pode realizar a exclusão.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode excluir outro usuário", content = @Content),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID do usuário", required = true, example = "1") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data){
        userService.deleteById(id, data.id());
        return ResponseEntity.noContent()
                .build();
    }

    @Operation(summary = "Listar serviços do usuário", description = "Retorna todos os serviços associados ao usuário informado. Apenas o próprio usuário pode acessar seus serviços.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Serviços encontrados", content = @Content(schema = @Schema(implementation = ServicesResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode acessar serviços de outro usuário", content = @Content)
    })
    @GetMapping("{userId}/user")
    public ResponseEntity<List<ServicesResponseDTO>> getServicesByUser(
            @Parameter(description = "ID do usuário", required = true, example = "1") @PathVariable Long userId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {
        List<ServicesResponseDTO> services = servicesService.getAllServicesByUserId(userId, data.id());
        return ResponseEntity.ok(services);
    }

    @Operation(summary = "Listar avaliações do usuário", description = "Retorna todas as avaliações recebidas pelo usuário informado. Apenas o próprio usuário pode acessar suas avaliações.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avaliações encontradas", content = @Content(schema = @Schema(implementation = RatingResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode acessar avaliações de outro usuário", content = @Content)
    })
    @GetMapping("/{userId}/ratings")
    public ResponseEntity<List<RatingResponseDTO>> getRatingsByUser(
            @Parameter(description = "ID do usuário", required = true, example = "1") @PathVariable Long userId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {
        List<RatingResponseDTO> ratings = ratingService.getAllRatingsByUserId(userId, data.id());
        return ResponseEntity.ok(ratings);
    }
}
