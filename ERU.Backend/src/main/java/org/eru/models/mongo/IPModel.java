package org.eru.models.mongo;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class IPModel {
    private ObjectId id;
    @BsonProperty("ip")
    public String Ip;

    @BsonProperty("token")
    public String Token;

    public IPModel(String ip, String token) {
        this.Ip = ip;
        this.Token = token;
    }

    public IPModel() {
    }
}
