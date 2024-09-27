package org.eru.models.mongo.user;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.HashMap;

public class User {

    private ObjectId id;

    @BsonProperty("createdAt")
    public Instant CreatedAt;

    @BsonProperty("banned")
    public boolean Banned;

    @BsonProperty("bannedReason")
    public String BannedReason;

    @BsonProperty("discordId")
    public String DiscordId;

    @BsonProperty("accountId")
    public String AccountId;

    @BsonProperty("username")
    public String Username;

    @BsonProperty("usernameLower")
    public String UsernameLower;

    @BsonProperty("email")
    public String Email;

    @BsonProperty("password")
    public String Password;

    @BsonProperty("profileChangesBaseRevision")
    public int ProfileChangesBaseRevision;
}