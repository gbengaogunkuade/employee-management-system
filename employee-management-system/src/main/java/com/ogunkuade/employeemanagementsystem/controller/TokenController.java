package com.ogunkuade.employeemanagementsystem.controller;


import com.ogunkuade.employeemanagementsystem.service.TokenServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TokenController {

    private final TokenServiceImplementation tokenServiceImplementation;

    public TokenController(TokenServiceImplementation tokenServiceImplementation) {
        this.tokenServiceImplementation = tokenServiceImplementation;
    }


    @PostMapping("/token")
    public ResponseEntity<String> gettingToken(@RequestParam String username, @RequestParam String password){
        return tokenServiceImplementation.getToken(username, password);
    }


    @GetMapping("/token-information")
    public ResponseEntity<Map<String, Object>> gettingTokenInfo(Authentication authentication, JwtAuthenticationToken jwtAuthenticationToken){
        return tokenServiceImplementation.getTokenInfo(authentication, jwtAuthenticationToken);
    }


}
