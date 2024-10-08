package com.socialLogin.socialLogin.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.security.Key;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${secret-key}")
    private String secretKey;

    //생성
    public String create(String userId) {

        //만료 기간
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        //JWT에서 생성, 검증할 때 사용하는 HMAC-SHA 알고리즘 기반의 비밀 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // JWT 생성
        String jwt = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)    // JWT에 서명 추가
                .setSubject(userId).setIssuedAt(new Date()).setExpiration(expiredDate)  // JWT의 sub claim 설정, 여기서는 userId로 설정, 및 setIssuedAt은 발행 시간(iat claim)
                .compact();

        return jwt;
    }

    //검증
    public String validate(String jwt) {
         String subject = null;

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            subject = claims.getSubject();

        }catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }

        return subject;
    }
}
