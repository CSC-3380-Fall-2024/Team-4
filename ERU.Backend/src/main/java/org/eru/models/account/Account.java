package org.eru.models.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

public class Account {
    @JsonProperty("id")
    public String Id;

    @JsonProperty("name")
    public String Name;

    @JsonProperty("displayName")
    public String DisplayName;

    @JsonProperty("externalAuths")
    public HashMap<String, String> ExternalAuths = new HashMap<>();

    @JsonProperty("posts")
    public List<Post> Posts;
}
