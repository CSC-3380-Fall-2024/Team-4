package org.eru.controllers.mcp;

import org.eru.controllers.mcp.operations.profileChanges.ProfileChange;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class McpResponse {
    @JsonProperty("profileRevision")
    public int ProfileRevision;

    @JsonProperty("profileId")
    public String ProfileId;

    @JsonProperty("profileChangesBaseRevision")
    public int ProfileChangesBaseRevision;

    @JsonProperty("profileChanges")
    public ArrayList<ProfileChange> ProfileChanges;

    @JsonProperty("profileCommandRevision")
    public int ProfileCommandRevision;

    @JsonProperty("serverTime")
    public String ServerTime;

    @JsonProperty("responseVersion")
    public int ResponseVersion;

    public McpResponse(int rvn, String profileId, ArrayList<ProfileChange> changes) {
        ProfileRevision = rvn + 1;
        ProfileId = profileId;
        ProfileChangesBaseRevision = rvn;
        ProfileChanges = changes;
        ProfileCommandRevision = rvn + 1;
        ServerTime = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(java.time.ZoneId.of("UTC")).format(java.time.Instant.now());
        ResponseVersion = 1;
    }

    public McpResponse(int profileRevision, String profileId, int profileChangesBaseRevision, ArrayList<ProfileChange> changes, int profileCommandRevision) {
        ProfileRevision = profileRevision;
        ProfileId = profileId;
        ProfileChangesBaseRevision = profileChangesBaseRevision;
        ProfileChanges = changes;
        ProfileCommandRevision = profileCommandRevision;
        ServerTime = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(java.time.ZoneId.of("UTC")).format(java.time.Instant.now());
        ResponseVersion = 1;
    }
}