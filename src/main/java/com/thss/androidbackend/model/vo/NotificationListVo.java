package com.thss.androidbackend.model.vo;

import java.io.Serializable;
import java.util.List;

public record NotificationListVo(
        List<NotificationVo> NotificationList
) implements Serializable {

}
