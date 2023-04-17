package com.ogunkuade.employeemanagementsystem.service;


import com.ogunkuade.employeemanagementsystem.entity.User;
import com.ogunkuade.employeemanagementsystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class TokenServiceImplementation {


    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    public TokenServiceImplementation(UserRepository userRepository, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }


    public ResponseEntity<String> getToken(String username, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenService.generateToken(authentication);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }



    public ResponseEntity<Map<String, Object>> getTokenInfo(Authentication authentication, JwtAuthenticationToken jwtAuthenticationToken){
        User user = userRepository.findByUsername(authentication.getName());

        List<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName()).collect(Collectors.toList());

        List<String> authoritiesNames = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList());

        Object tokenAttribute = jwtAuthenticationToken.getTokenAttributes();
        Instant expiryDate = tokenService.getExpiryDate();

        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("tokenAttribute", tokenAttribute);
        tokenInfo.put("expiryDate", expiryDate);
        tokenInfo.put("username", authentication.getName());
        tokenInfo.put("firstname", user.getFirstname());
        tokenInfo.put("lastname", user.getLastname());
        tokenInfo.put("gender", user.getGender());
        tokenInfo.put("email", user.getEmail());
        tokenInfo.put("roles", roleNames);
        tokenInfo.put("authorities", authoritiesNames);
        return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);
    }



}
