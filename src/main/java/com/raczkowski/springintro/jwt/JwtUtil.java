package com.raczkowski.springintro.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import static io.jsonwebtoken.Jwts.builder;

@Component
public class JwtUtil {

    public String generateToken(String username) {
        return builder()
                .signWith(SignatureAlgorithm.HS512, "my-secret")
                .setSubject(username)
                .compact();
    }

}
