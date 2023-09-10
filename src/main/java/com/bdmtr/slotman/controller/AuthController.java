package com.bdmtr.slotman.controller;

import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.security.CustomUserDetails;
import com.bdmtr.slotman.security.JwtTokenProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody User loginUser) {
        try {
            log.info("User '{}' successfully authenticated.", loginUser.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String jwtToken = jwtTokenProvider.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(jwtToken);

        } catch (UsernameNotFoundException ex) {
            log.error("Authentication failed for user '{}'.", loginUser.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
}