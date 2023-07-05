package com.kosa.gather_e.util;

import com.kosa.gather_e.auth.vo.UserVO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtils {

    private static Key key;

    public JwtTokenUtils(@Value("${jwt.secret}") String secretKey) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateJwtToken(UserVO user) {
        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(user))
                .setSubject(String.valueOf(user.getUserName()))
                .signWith(key)
                .setExpiration(createExpiredDate()).compact();
    }

    public String parseTokenToUser(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.out.println("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            System.out.println("unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

    public String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    public Date createExpiredDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 10);
        return c.getTime();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();

        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private Map<String, Object> createClaims(UserVO user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("userSeq", user.getUserSeq());
        claims.put("userEmail", user.getUserEmail());
        return claims;
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody();
    }

    public String getUserSeqFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userSeq").toString();
    }
}
