package com.assessment.maybank.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${assessment.auth.jwt.secret}")
    private String SECRET_KEY;

    @Value("${assessment.auth.jwt.expiration}")
    private long EXPIRATION_TIME;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // Create JWT token base on username, issued date, and expiration date & sign with key
        JwtBuilder builder = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY);

        return builder.compact();
    }

    public Claims validateToken(String token) {

        //Validate the token & check if it's expired
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token).getBody();
    }
}