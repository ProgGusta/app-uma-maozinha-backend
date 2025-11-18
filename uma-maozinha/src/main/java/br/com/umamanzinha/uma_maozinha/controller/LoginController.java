package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.dtos.login.LoginRequest;
import br.com.umamanzinha.uma_maozinha.dtos.login.LoginResponse;
import br.com.umamanzinha.uma_maozinha.dtos.services.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping()
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws AuthenticationException {
        return ResponseEntity.ok(loginService.login(loginRequest)) ;
    }

}
