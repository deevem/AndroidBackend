package com.thss.androidbackend.model.document;

import com.thss.androidbackend.model.vo.NotificationVo;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class NotificationMessage {
    @Id
    @Indexed(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @DBRef(lazy = true)
    private User userToNotify;
    @DBRef(lazy = true)
    private User creator;
    private String route;
    private String title;
    private String content;
    private boolean readFlag;
    public NotificationVo getNotificationVo() {
        return new NotificationVo(
                userToNotify.getMeta(),
                creator.getMeta(),
                route,
                title,
                content,
                readFlag
        );
    }
}
