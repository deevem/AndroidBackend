package com.thss.androidbackend.model.vo.user;

import java.io.Serializable;

public record UserMeta (
    String userId,
    String userName ,
    String userIconUrl

) implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserMeta)) return false;
        UserMeta userMeta = (UserMeta) o;
        return userId.equals(userMeta.userId);
    }
}

