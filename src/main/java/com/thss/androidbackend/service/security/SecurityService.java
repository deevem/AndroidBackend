package com.thss.androidbackend.service.security;

import com.thss.androidbackend.model.document.User;

public interface SecurityService {
    User getCurrentUser();
    String getCurrentUsername();
}
