package com.alshadows.product.services;

import com.alshadows.product.dto.auth.LoginRequest;
import com.alshadows.product.dto.auth.LoginResponse;
import com.alshadows.product.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public LoginResponse authenticate(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String jwt = jwtService.generateToken(userDetails);

            return LoginResponse.builder()
                    .token(jwt)
                    .username(userDetails.getUsername())
                    .roles(userDetails.getAuthorities())
                    .build();

        } catch (AuthenticationException e) {
            log.error("Erreur d'authentification: {}", e.getMessage());
            throw new AuthenticationException("Nom d'utilisateur ou mot de passe incorrect") {};
        }
    }
}

