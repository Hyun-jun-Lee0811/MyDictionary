package com.github.Hyun_jun_Lee0811.dictionary.security;

import static com.github.Hyun_jun_Lee0811.dictionary.type.ErrorCode.*;

import com.github.Hyun_jun_Lee0811.dictionary.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtTokenProvider {

  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
  private final SecretKey secretKey;

  @Autowired
  private UserService userService;

  @Autowired
  public JwtTokenProvider(@Value("${jwt.secret.Key}") String secretKey) {
    this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String username) {
    Date currentDate = new Date();

    return Jwts.builder()
        .subject(username)
        .issuedAt(currentDate)
        .expiration(new Date(currentDate.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = this.userService.loadUserByUsername(this.getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    Claims claims = this.parseClaims(token);
    if (claims == null || claims.getSubject() == null) {
      throw new RuntimeException(String.valueOf(INVALID_JWT_TOKEN));
    }
    return claims.getSubject();
  }

  public boolean validateToken(String token) {
    if (!StringUtils.hasText(token)) {
      return false;
    }

    Claims claims = this.parseClaims(token);
    if (claims == null || claims.getExpiration() == null) {
      return false;
    }

    return !claims.getExpiration().before(new Date());
  }

  private Claims parseClaims(String token) {
    try {
      return Jwts.parser()
          .verifyWith(secretKey)
          .build()
          .parseSignedClaims(token)
          .getPayload();

    } catch (SignatureException e) {
      log.error(INVALID_JWT_SIGNATURE.getMessage(), e);
    } catch (MalformedJwtException e) {
      log.error(INVALID_JWT_TOKEN.getMessage(), e);
    } catch (ExpiredJwtException e) {
      log.error(EXPIRED_JWT_TOKEN.getMessage(), e);
    } catch (UnsupportedJwtException e) {
      log.error(UNSUPPORTED_JWT_TOKEN.getMessage(), e);
    }
    return null;
  }

}
