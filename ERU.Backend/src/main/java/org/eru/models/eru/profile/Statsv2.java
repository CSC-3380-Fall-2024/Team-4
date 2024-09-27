package org.eru.models.eru.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class Statsv2 {
    @JsonProperty("startTime")
    public int StartTime;

    @JsonProperty("endTime")
    public int EndTime;

    @JsonProperty("stats")
    public HashMap<String, String> Stats;

    @JsonProperty("accountId")
    public String AccountId;

    public Statsv2(String accountId) {
        AccountId = accountId;
    }
}
