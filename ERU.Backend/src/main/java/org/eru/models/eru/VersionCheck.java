package org.eru.models.eru;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VersionCheck {
    @JsonProperty("type")
    public String Type;

    public VersionCheck(String type) {
        this.Type = type;
    }
}
