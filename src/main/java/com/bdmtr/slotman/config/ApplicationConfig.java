package com.bdmtr.slotman.config;

import com.bdmtr.slotman.exception.UserNotFoundException;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.repository.UserRepository;
import com.bdmtr.slotman.model.service.UserService;
import com.bdmtr.slotman.security.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {
    private final UserRepository repository;

    public ApplicationConfig(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a custom implementation of the `UserDetailsService` interface.
     * This method retrieves user details from the database based on the username and constructs a `CustomUserDetails` object.
     * @see CustomUserDetails
     *
     * @return A custom `UserDetailsService` implementation.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = repository.findByUsername(username)
                    .orElseThrow(() -> new UserNotFoundException("Cant find user: " + username));
            return new CustomUserDetails(user);
        };
    }

    /**
     * Configures an `AuthenticationProvider` for authentication purposes.
     * It uses a `DaoAuthenticationProvider` with a custom `UserDetailsService` and a password encoder.
     *
     * @return An `AuthenticationProvider` instance.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Creates an `AuthenticationManager` bean, which is responsible for handling authentication requests.
     *
     * @param config The `AuthenticationConfiguration` provided by Spring.
     * @return An `AuthenticationManager` instance.
     * @throws Exception If there is an issue with authentication configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();
        //return new BCryptPasswordEncoder();
    }

}
