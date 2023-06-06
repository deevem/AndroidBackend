package com.thss.androidbackend.config;

import com.thss.androidbackend.exception.FilterChainExceptionHandler;
import com.thss.androidbackend.service.security.JwtAuthorizationFilter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.time.Duration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    public static final Duration EXPIRATION_TIME = Duration.ofDays(5);
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final FilterChainExceptionHandler filterChainExceptionHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .accessDeniedHandler((request, response, accessDeniedException) -> response.setStatus(HttpStatus.FORBIDDEN.value())).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .logout().logoutUrl("/login/logout").and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST,"/login/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/register/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/users/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/posts/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/image/**").permitAll()
                .anyRequest().authenticated().and()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);
//                .apply(securityConfigurationAdapter());
        return http.build();

    }

}
