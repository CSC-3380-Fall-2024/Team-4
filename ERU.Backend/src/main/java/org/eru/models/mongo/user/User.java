package org.eru.models.mongo.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.List;

public class User {

    private ObjectId id;

    @BsonProperty("account_id")
    @JsonProperty("account_id")
    public String AccountId;

    @JsonProperty("name")
    public String Name;

    @BsonProperty("display_name")
    @JsonProperty("display_name")
    public String DisplayName;

    @BsonProperty("external_auths")
    @JsonProperty("external_auths")
    public HashMap<String, String> ExternalAuths = new HashMap<>();

    @JsonProperty("posts")
    public List<Post> Posts;

    @JsonProperty("email")
    public String Email;

    @JsonIgnore
    public String Password;

    @JsonProperty("banned")
    public boolean Banned;

    @JsonProperty("profile_picture")
    public String ProfilePicture;
}