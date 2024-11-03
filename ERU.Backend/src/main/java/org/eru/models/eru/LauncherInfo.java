package org.eru.models.eru;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LauncherInfo {
    @JsonProperty("appName")
    public String AppName = "ERU";

    @JsonProperty("catalogItemId")
    public String CatalogItemId = "4fe75bbc5a674f4f9b356b5c90567da5";

    @JsonProperty("namespace")
    public String Namespace = "eru";
}
