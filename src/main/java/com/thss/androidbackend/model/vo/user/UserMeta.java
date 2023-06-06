package com.thss.androidbackend.model.vo.user;

import java.io.Serializable;

public record UserMeta (
    String id,
    String avatar,
    String nickname

) implements Serializable {

}

