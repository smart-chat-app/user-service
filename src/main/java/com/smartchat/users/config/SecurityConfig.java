package com.smartchat.users.config;

import com.smartchat.users.utils.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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
                // Fallback: accept "Bearer <userId>"
                String userId = Utils.getUserIdFromToken(token); // e.g., first 26 chars rule if you need it
                Instant now = Instant.now();

                Map<String, Object> headers = Map.of("alg", "none");
                Map<String, Object> claims = new HashMap<>();
                claims.put("sub", userId);
                claims.put("token_type", "userId");
                // (Optional) add any claims your app expects, e.g. username/displayName, scopes, etc.

                // Build a synthetic Jwt valid for 12h (adjust as you wish)
                return new Jwt(token, now, now.plus(Duration.ofHours(12)), headers, claims);
            }
        };
    }
}
