package org.eru.models.mongo.user;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;

public class Client {
    private ObjectId id;

    @BsonProperty("clientId")
    public String ClientId;

    @BsonProperty("clientSecret")
    public String ClientSecret;

    @BsonProperty("clientName")
    public String ClientName;

    @BsonProperty("redirectUrl")
    public String RedirectUrl;

    @BsonProperty("internal")
    public boolean internal;

    @BsonProperty("type")
    public String ClientType;

    @BsonProperty("clientService")
    public String ClientService;

    @BsonProperty("verified")
    public boolean Verified;

    @BsonProperty("allowedScopes")
    public List<String> AllowedScopes;

    @BsonProperty("grant_types")
    public List<String> GrantTypes;

    @BsonProperty("perms")
    public Perms Perms;

    @BsonProperty("disabled")
    public boolean Disabled;
}

