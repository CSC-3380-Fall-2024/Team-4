package org.eru.models.mongo.user;

import org.bson.codecs.pojo.annotations.BsonProperty;

public class Resource {
    @BsonProperty("resource")
    public String Resource;

    @BsonProperty("action")
    public int Action;
}
