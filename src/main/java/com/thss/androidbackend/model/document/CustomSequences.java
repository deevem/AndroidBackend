package com.thss.androidbackend.model.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "customSequences")
public class CustomSequences {
    @Id
    private String id;
    private int seq;

// getters and setters
}