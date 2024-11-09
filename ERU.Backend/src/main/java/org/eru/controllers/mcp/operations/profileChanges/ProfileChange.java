package org.eru.controllers.mcp.operations.profileChanges;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileChange {
    @JsonProperty("changeType")
    public String ChangeType;

    @JsonProperty("name")
    public String Name;

    public ProfileChange(String changeType, String name) {
        ChangeType = changeType;
        Name = name;
    }
}
