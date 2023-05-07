package com.thss.androidbackend.model.document.projections;

import com.thss.androidbackend.model.document.User;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "usermeta", types = {User.class} )
public interface Usermeta {
    String getUsername();
    String getUserIconUrl();
    String getNickname();
}
