package org.eru.models.mongo.user;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

public class Perms {
    @BsonProperty("account")
    public List<Resource> Account;

    @BsonProperty("client")
    public List<Resource> Client;
}

