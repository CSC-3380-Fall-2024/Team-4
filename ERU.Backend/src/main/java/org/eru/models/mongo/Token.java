package org.eru.models.mongo;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Token {
    private ObjectId id;
    @BsonProperty("accountId")
    public String AccountId;

    @BsonProperty("token")
    public String Token;

    public Token(String accountId, String token) {
        this.AccountId = accountId;
        this.Token = token;
    }

    public Token() {
    }
}
