package com.github.nikhilsutarin.usermanagementsystem.jwt;

import com.github.nikhilsutarin.usermanagementsystem.model.Role;
import com.github.nikhilsutarin.usermanagementsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Value("${jwt.expiration:86400}")
    private Long expiration;

    public String generateToken(User user) {

        Set<String> roles = Optional.of(user.getRoles())
                .orElse(Set.of())
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getEmail())
                .claim("roles", roles)
                .issuer("user-management-system")
                .issuedAt(now)
                .expiresAt(now.plus(expiration, ChronoUnit.SECONDS))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                .type("JWT")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
