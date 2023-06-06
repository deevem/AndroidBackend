package com.thss.androidbackend.service.login;

import com.thss.androidbackend.config.SecurityConfig;
import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.login.EmailLoginDto;
import com.thss.androidbackend.model.dto.login.PhoneLoginDto;
import com.thss.androidbackend.model.dto.login.UsernameLoginDto;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.login.LoginService;
import com.thss.androidbackend.service.security.JwtUtils;
import com.thss.androidbackend.service.security.SecurityService;
import com.thss.androidbackend.service.user.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    public String login(@NotNull UsernameLoginDto dto){
        User user = userRepository.findByUsername(dto.username());

        if (Objects.isNull(user)) throw new UsernameNotFoundException("User not found");

        if (passwordEncoder.matches(dto.password(),user.getPassword())) {
            List<String> roles = user.getRoles();
            if (roles.isEmpty()) roles = Collections.singletonList("ROLE_USER");
            String token = JwtUtils.generateJwtToken(user.getUsername(), roles);

            user.setLastLoginTime(LocalDateTime.now());
            userRepository.save(user);

            if(redisTemplate.hasKey(user.getUsername())) {
                redisTemplate.delete(user.getUsername());
            }
            redisTemplate.opsForValue().set(user.getUsername(), token, SecurityConfig.EXPIRATION_TIME);

            Authentication authentication = JwtUtils.getAuthentication(user, token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return token;
        }
        throw new BadCredentialsException("Wrong password or username");

    }
    public String login(@NotNull EmailLoginDto dto){
        return null;
    }
    public String login(@NotNull PhoneLoginDto dto){
        return null;
    }

    public void logout(){
        redisTemplate.delete(securityService.getCurrentUsername());
        SecurityContextHolder.clearContext();
    }
}
