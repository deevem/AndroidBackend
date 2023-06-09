package com.thss.androidbackend.service.security;

import com.thss.androidbackend.exception.CustomException;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

public class JwtUtils {
    private static String key = "ThisKeyMustBeAtLeast256BitsLongToUseForHMAC256";
    public static String generateJwtToken(String username, List<String> roles) {
        String token =
                Jwts.builder()
                        .setHeaderParam("typ", "JWT")
                        .claim("role", roles)
                        .setAudience("security-all")
                        .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 2L * 1000))
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setSubject(username)
                        .signWith(Keys.hmacShaKeyFor(key.getBytes()), SignatureAlgorithm.HS256)
                        .compact();
        return token;
    }

    public static Claims parse(String token){
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key.getBytes()).build().parseClaimsJws(token).getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new CustomException(HttpStatus.UNAUTHORIZED, e.getClass().toString().substring(6));
        }
    }

    public static boolean verify(String token){
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key.getBytes()).build().parseClaimsJws(token).getBody();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    public static Authentication getAuthentication(User user, String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key.getBytes()).build().parseClaimsJws(token).getBody();
        List<String> roles = claims.get("role", List.class);
        List<SimpleGrantedAuthority> authorities = Objects.isNull(roles) ? Collections.singletonList(new SimpleGrantedAuthority("user")) : roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(user, token, authorities);
    }
}
