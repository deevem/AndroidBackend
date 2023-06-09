package com.thss.androidbackend.model.document;

import com.thss.androidbackend.model.vo.NotificationVo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class NotificationMessage {
    @DBRef(lazy = true)
    private User userToNotifiy;
    @DBRef(lazy = true)
    private User creator;
    private String route;
    private String title;
    private String content;

    public NotificationVo getNotificationVo() {
        return new NotificationVo(
                userToNotifiy.getMeta(),
                creator.getMeta(),
                route,
                title,
                content
        );
    }
}
