package org.eru.managers;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtGenerator {
    private static JwtGenerator INSTANCE = null;
    public static JwtGenerator getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new JwtGenerator();
            }
            catch (Exception e) {
                System.out.println("Error creating JwtGenerator instance: " + e.getMessage());
            }
        }

        return INSTANCE;
    }
    private UUID SECRET = UUID.randomUUID();
    public String generateJwt(Map<String, String> payload, Instant expiresAt) {
        Builder tokenBuilder = JWT.create()
                .withExpiresAt(expiresAt);

        payload.forEach(tokenBuilder::withClaim);
        return tokenBuilder.sign(Algorithm.HMAC256(SECRET.toString()));
    }

    public DecodedJWT decodeJwt(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET.toString())).build().verify(token);
    }
}
