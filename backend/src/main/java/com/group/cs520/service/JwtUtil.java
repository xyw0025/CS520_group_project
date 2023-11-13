package com.group.cs520.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtUtil implements InitializingBean {

    @Value("${jwt.secret}")
    private String jwtSecret;
    private Algorithm algorithm;

    @Override
    public void afterPropertiesSet() {
        this.algorithm = Algorithm.HMAC256(jwtSecret);
    }

    public String extractUserId(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getSubject();
    }

    public String createToken(String email) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24 * 60 * 60 * 1000); // expire after one day

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }
}