package org.eru.models.mongo;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.eru.models.account.JTICreationExpiration;

public class IPModel {
    private ObjectId id;
    @BsonProperty("ip")
    public String Ip;

    @BsonProperty("token")
    public JTICreationExpiration Token;

    public IPModel(String ip, JTICreationExpiration token) {
        this.Ip = ip;
        this.Token = token;
    }

    public IPModel() {
    }
}
