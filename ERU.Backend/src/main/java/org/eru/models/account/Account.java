package org.eru.models.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class Account {
    @JsonProperty("id")
    public String Id;

    @JsonProperty("displayName")
    public String DisplayName;

    @JsonProperty("externalAuths")
    public HashMap<String, String> ExternalAuths = new HashMap<>();
}
