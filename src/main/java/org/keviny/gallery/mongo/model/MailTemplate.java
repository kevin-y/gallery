package org.keviny.gallery.mongo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * Created by kevin on 6/6/15.
 */
public class MailTemplate implements Serializable {

    @JsonProperty("_id")
    private ObjectId id;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }
}
