package com.thss.androidbackend.service.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        setAuthorization(request);
        filterChain.doFilter(request, response);
    }

    private void setAuthorization(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")){
            return ;
        }
        String token = authorization.substring(7);
        Claims claims = JwtUtils.parse(token);
        if (!Objects.isNull(claims)){
            String username = claims.getSubject();
            String redisToken = redisTemplate.opsForValue().get(username);
            if (redisToken == null || !redisToken.equals(token)){
                System.out.println("username:" + username);
                System.out.println("redisToken: " + redisToken);
                System.out.println("token:      " + token);
                return ;
            }
            Authentication auth = JwtUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

    }
}
