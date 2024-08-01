package com.steadon.saber.model.dao;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@Document(collection = "log")
public class Log {
    @MongoId(FieldType.OBJECT_ID)
    private String _id;
    @Indexed
    private String traceId;
    private Long timestamp;
    private String level;
    private String thread;
    private String method;
    private String message;
    @Indexed(expireAfterSeconds = 15 * 86400)
    private Date createdAt;

}
