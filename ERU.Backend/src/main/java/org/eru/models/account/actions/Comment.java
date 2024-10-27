package org.eru.models.account.actions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Comment extends AccountAction {
    @JsonProperty("content")
    public String Content;

    @JsonProperty("likes")
    public List<Like> Likes;
}
