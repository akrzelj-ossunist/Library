package com.maurer.library.utils;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class TokenGenerator {
    private final JwtEncoder encoder;

    public TokenGenerator(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken(String authority, String name) {
        Instant now = Instant.now();
        System.out.println("Authority: " + authority);
        System.out.println("User name: " + name);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(name)
                .claim("scope", authority) // Using the authority parameter directly
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
