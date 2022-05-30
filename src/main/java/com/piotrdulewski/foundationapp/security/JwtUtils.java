package com.piotrdulewski.foundationapp.security;

import com.piotrdulewski.foundationapp.dto.LoginUserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final String jwtSecret = "n4$lJ*jDAl<spjj";

    private static final int jwtExpirationMs = 5 * 60 * 1000;

    public String generateJwtToken(LoginUserDTO userDTO) {
        return generateTokenFromUsername(userDTO.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", "ROLE_USER")
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes()).compact();
    }
}