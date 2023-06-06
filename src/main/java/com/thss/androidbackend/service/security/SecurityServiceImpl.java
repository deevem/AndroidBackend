package com.thss.androidbackend.service.security;

import com.thss.androidbackend.model.document.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }
    public boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication == null || authentication.getPrincipal() == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass()));
    }
}
