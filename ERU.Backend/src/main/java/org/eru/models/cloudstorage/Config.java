package org.eru.models.cloudstorage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.Hashtable;

public class Config {
    @JsonProperty("lastUpdated")
    public String LastUpdated;

    @JsonProperty("disableV2")
    public boolean DisableV2 = true;

    @JsonProperty("isAuthenticated")
    public boolean IsAuthenticated = true;

    @JsonProperty("enumerateFilesPath")
    public String EnumerateFilesPath = "/api/cloudstorage/system";

    @JsonProperty("transports")
    public Dictionary<String, ConfigTransport> Transports;

    public Config() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
        LastUpdated = dtf.format(Instant.now());

        Transports = new Hashtable<String, ConfigTransport>()
        {
            {
                put("McpProxyTransport", new ConfigTransport("McpProxyTransport", "ProxyStreamingFile", false, 10));
                put("McpSignatoryTransport", new ConfigTransport("McpSignatoryTransport", "ProxySignatory", false, 20));
                put("DssDirectTransport", new ConfigTransport("DssDirectTransport", "DirectDss", false, 30));
            }
        };
    }
}
