package org.eru.models.eru;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Lightswitch {
    @JsonProperty("serviceInstanceId")
    public String ServiceInstanceId;

    @JsonProperty("status")
    public String Status = "UP";

    @JsonProperty("message")
    public String Message = "Eru is online";

    @JsonProperty("maintenanceUri")
    public String MaintenanceUri = "https://eru.org";

    @JsonProperty("overrideCatalogIds")
    public String[] OverrideCatalogIds = new String[] { "a7f138b2e51945ffbfdacc1af0541053" };

    @JsonProperty("allowedActions")
    public String[] AllowedActions;

    @JsonProperty("banned")
    public boolean Banned = false;

    @JsonProperty("launcherInfoDTO")
    public LauncherInfo LauncherInfoDTO = new LauncherInfo();

    public Lightswitch(String serviceId, String[] allowedActions) {
        ServiceInstanceId = serviceId;
        AllowedActions = allowedActions;
    }
}
