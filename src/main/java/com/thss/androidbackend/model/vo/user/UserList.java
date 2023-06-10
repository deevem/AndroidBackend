package com.thss.androidbackend.model.vo.user;

import java.io.Serializable;
import java.util.List;

public record UserList(
        List<UserVo> users
) implements Serializable  {

}
