package com.thss.androidbackend.model.vo.user;

import java.io.Serializable;

public record UserMeta (
    String userId,
    String userName,
    String userIconUrl

) implements Serializable {

}

