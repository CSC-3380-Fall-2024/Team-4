package org.eru.models.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;
import java.util.List;

public class Account {

    @BsonProperty("account_id")
    @JsonProperty("account_id")
    public String Id;

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
}
