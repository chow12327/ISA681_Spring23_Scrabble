package com.isa681.scrabble.controller;

import com.isa681.scrabble.entity.JwtTokenRequest;
import com.isa681.scrabble.entity.JwtTokenResponse;
import com.isa681.scrabble.exceptions.InvalidUserDetailsException;
import com.isa681.scrabble.service.JwtTokenService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="https://localhost:4043")
public class JwtAuthenticationController {

    private static final Logger myLogger = LogManager.getLogger(GameController.class);

    private final JwtTokenService tokenService;

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationController(JwtTokenService tokenService,
                                       AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> generateToken(
            @RequestBody JwtTokenRequest jwtTokenRequest) throws InvalidUserDetailsException {

        myLogger.info("Login request received from " + jwtTokenRequest.username());

        try {
            ValidationController.validateJwtTokenRequestRequest(jwtTokenRequest);
        }
        catch(Exception e)
        {
            throw(e);
        }
        var authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        jwtTokenRequest.username(),
                        jwtTokenRequest.password());

        var authentication =
                authenticationManager.authenticate(authenticationToken);

        var token = tokenService.generateToken(authentication);

        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
}

