package com.thss.androidbackend.service.security;

import com.thss.androidbackend.model.document.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }
}
