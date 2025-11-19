package com.smartchat.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/health", "/actuator/**", "/v3/api-docs/**", "/swagger-ui/**", "/users/create")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt()); // uses our JwtDecoder bean
        return http.build();
    }

    /**
     * Decoder that first tries real JWT via JWKS, then falls back to treating the token
     * string itself as a userId (no signature verification).
     */
    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwks,
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")    String issuer) {

        // Primary: real JWT from Keycloak (or any OIDC provider)
        NimbusJwtDecoder nimbus = NimbusJwtDecoder.withJwkSetUri(jwks).build();
        nimbus.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));

        // Wrapper that falls back to "userId token"
        return token -> {
            try {
                return nimbus.decode(token);
            } catch (JwtException ex) {
                throw new RuntimeException("User not authenticated");
            }
        };
    }
}
