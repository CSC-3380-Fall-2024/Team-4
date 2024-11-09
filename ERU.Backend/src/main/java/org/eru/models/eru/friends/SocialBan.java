package org.eru.models.eru.friends;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class SocialBan {
    @JsonProperty("bans")
    public ArrayList<String> Bans;

    @JsonProperty("warnings")
    public ArrayList<String> Warnings;

    public SocialBan(ArrayList<String> bans, ArrayList<String> warnings) {
        Bans = bans;
        Warnings = warnings;
    }
}
