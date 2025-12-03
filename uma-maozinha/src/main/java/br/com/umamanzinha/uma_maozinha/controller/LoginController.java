package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.dtos.login.LoginRequest;
import br.com.umamanzinha.uma_maozinha.dtos.login.LoginResponse;
import br.com.umamanzinha.uma_maozinha.services.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação", description = "Operação de login para autenticação de usuários e geração de token JWT.")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Realiza o login do usuário.
     * <br>Regras de negócio:
     * <ul>
     *   <li>O e-mail e senha informados são validados.</li>
     *   <li>Se inválidos, retorna erro de autenticação (401).</li>
     *   <li>Se válidos, retorna um token JWT válido por 1 hora, contendo informações do usuário e roles.</li>
     * </ul>
     * @param loginRequest Dados de login (e-mail e senha).
     * @return LoginResponse com token JWT e tempo de expiração.
     */
    @Operation(summary = "Login do usuário", description = "Autentica o usuário e retorna um token JWT para acesso às rotas protegidas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou usuário não encontrado", content = @Content)
    })
    @PostMapping()
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Dados de login (e-mail e senha)", required = true) @RequestBody LoginRequest loginRequest) throws AuthenticationException {
        return ResponseEntity.ok(loginService.login(loginRequest)) ;
    }

}
