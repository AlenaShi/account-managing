package com.hes.account.service;

import com.hes.account.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Used for working with jwt tokens
 */
@Service
public class JwtServiceImpl {

    public static final String NAME = "name";
    public static final String BEARER = "Bearer";
    public static final int BEGIN_INDEX = 6;
    public static final int EXPIRE_TIME = 1000000 * 60 * 24;
    Logger logger = LogManager.getLogger();
    @Value("${token.signing.key}")
    private String jwtSigningKey;

    /**
     * Used to generate token
     *
     * @param user user for which token should be generated
     * @return token
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(NAME, user.getName());
        String token = generateToken(claims, user);
        logger.info(token);
        return token;
    }

    /**
     * Check if token is valid
     *
     * @param token
     * @param userDetails user information
     * @return true if for current user and not expired
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Check if token expired
     *
     * @param token
     * @return true if expired
     */
    public boolean isTokenExpired(String token) {
        Claims claims = getTokenClaims(token);
        Date expiredDate = claims.getExpiration();
        Date currentDate = new Date(System.currentTimeMillis());
        return expiredDate.compareTo(currentDate) < 0;
    }

    /**
     * Used to extract username from token
     *
     * @param token
     * @return user name
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Used to extract claims from token
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return claims
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getTokenClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getTokenClaims(String token) {
        if (token.contains(BEARER)) {
            token = token.substring(BEGIN_INDEX).strip();
        }
        return Jwts.parser().verifyWith(decodeSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Used to generate token
     *
     * @param extraClaims
     * @param user
     * @return token
     */
    private String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts.builder().claims(extraClaims).subject(user.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(decodeSigningKey()).compact();
    }

    private SecretKey decodeSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
