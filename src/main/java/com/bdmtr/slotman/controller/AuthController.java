package com.bdmtr.slotman.controller;

import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.security.CustomUserDetails;
import com.bdmtr.slotman.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class responsible for handling authentication-related requests.
 */
@Log4j2
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("http://localhost:8081")
@Tag(name = "AuthController", description = "Controller class responsible for handling authentication-related requests.")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Constructs a new AuthController instance.
     *
     * @param authenticationManager The authentication manager for user authentication.
     * @param jwtTokenProvider     The JWT token provider for token generation and validation.
     */
    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param loginUser The user's login credentials (username and password) as a User object.
     * @return A ResponseEntity containing a JWT token if authentication is successful, or an error message if authentication fails.
     */
    @PostMapping("/login")
    @Operation(description = "Authenticates a user based on the provided credentials")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "User successfully authenticated."),
            @ApiResponse(responseCode = "401", description = "Failed authentication")
                } )
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