package in_.apcfss.util;


import in_.apcfss.enums.JWTEnum;
import in_.apcfss.exception.AuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//@Component
@RequiredArgsConstructor
public class JWTTokenUtil {

    private static final Logger log = LoggerFactory.getLogger(JWTTokenUtil.class);


    public static final long MILLI_SECONDS_PER_ONE_MIN = 60000;

    @Value("${jwt.accessToken.expiryTimeMinutes}")
    private long accessTokenExpiryTime;
    @Value("${jwt.refreshToken.expiryTimeMinutes}")
    private long refreshTokenExpiryTime;

    @Value("${jwt.accessToken.signKey.secretKey}")
    private String accessTokenSecretKey;
    @Value("${jwt.refreshToken.signKey.secretKey}")
    private String refreshTokenSecretKey;

    /**
     * ACCESS TOKEN
     */
    public String generateToken(String userName, String userEmail, String userType, Long orgId) {
//        String userType, Long orgId
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWTEnum.USER_NAME.getValue(), userName);
        claims.put(JWTEnum.USER_EMAIL.getValue(), userEmail);
        claims.put(JWTEnum.AUTHORITIES_CLAIM.getValue(), JWTEnum.ACCESS_TOKEN_ROLE.getValue());
        claims.put(JWTEnum.SESSION_TIMEOUT.getValue(), 1800);
        claims.put(JWTEnum.USER_ID.getValue(), orgId);
        claims.put(JWTEnum.USER_TYPE.getValue(), userType);
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(currentTimeMillis + accessTokenExpiryTime * MILLI_SECONDS_PER_ONE_MIN))
                .signWith(getAccessTokenSignKey(), Jwts.SIG.HS512)
                .compact();
    }


    /**
     * REFRESH TOKEN
     */
    public String generateRefreshToken(String userName, String userEmail, String userType, Long orgId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWTEnum.USER_ID.getValue(), orgId);
        claims.put(JWTEnum.USER_NAME.getValue(), userName);
        claims.put(JWTEnum.USER_EMAIL.getValue(), userEmail);
        claims.put(JWTEnum.USER_TYPE.getValue(), userType);
        claims.put(JWTEnum.AUTHORITIES_CLAIM.getValue(), JWTEnum.REFRESH_TOKEN_ROLE.getValue());

        return createRefreshToken(claims, userName);
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(currentTimeMillis + refreshTokenExpiryTime * MILLI_SECONDS_PER_ONE_MIN))
                .signWith(getRefreshTokenSignKey(), Jwts.SIG.HS512)
                .compact();
    }

    /**
     * UTILS
     * <p>
     * KEYS GENERATION -->
     * MacAlgorithm alg = Jwts.SIG.HS512; //or HS384 or HS256
     * return alg.key().build();
     */
    private SecretKey getAccessTokenSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(accessTokenSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getRefreshTokenSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(refreshTokenSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUserName(String token) {
        return getClaimFromToken(token, this.getClaimsResolver(JWTEnum.USER_NAME.getValue(), String.class));
    }

    public String getUserEmail(String token) {
        return getClaimFromToken(token, this.getClaimsResolver(JWTEnum.USER_EMAIL.getValue(), String.class));
    }

    public String getUserType(String token) {
        return getClaimFromToken(token, this.getClaimsResolver(JWTEnum.USER_TYPE.getValue(), String.class));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> Function<Claims, T> getClaimsResolver(String claimName, Class<T> requiredType) {
        return claims -> claims.get(claimName, requiredType);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseJwtClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims parseJwtClaims(String token) {
        return Jwts.parser()
                .verifyWith(getAccessTokenSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateJWTToken(String token) {
        try {
            if (this.getExpirationDateFromToken(token).compareTo(new Date()) > 0) {
                return true;
            }
            throw new AuthenticationException("JWT token is expired");
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String getRTUserName(String token) {
        return getClaimFromRT(token, this.getClaimsResolver(JWTEnum.USER_NAME.getValue(), String.class));
    }

    public String getRTUserEmail(String token) {
        return getClaimFromRT(token, this.getClaimsResolver(JWTEnum.USER_EMAIL.getValue(), String.class));
    }
    public String getRTUserType(String token) {
        return getClaimFromRT(token, this.getClaimsResolver(JWTEnum.USER_TYPE.getValue(), String.class));
    }

    public Long getRTUserId(String token) {
        return getClaimFromRT(token, this.getClaimsResolver(JWTEnum.USER_ID.getValue(), Long.class));
    }

    public Date getRTExpirationDate(String token) {
        return getClaimFromRT(token, Claims::getExpiration);
    }

    public <T> T getClaimFromRT(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseRTClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims parseRTClaims(String token) {
        return Jwts.parser()
                .verifyWith(getRefreshTokenSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateRefreshToken(String token) {
        try {
            if (this.getRTExpirationDate(token).compareTo(new Date()) > 0) {
                return true;
            }
            throw new AuthenticationException("JWT token is expired");
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
