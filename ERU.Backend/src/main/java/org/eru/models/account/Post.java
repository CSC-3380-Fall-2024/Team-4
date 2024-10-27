package org.eru.models.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eru.models.account.actions.Comment;
import org.eru.models.account.actions.Like;

import java.util.List;

public class Post {
    // encoded with b64
    @JsonProperty("content")
    public String Content;

    @JsonProperty("likes")
    public List<Like> Likes;

    @JsonProperty("comments")
    public List<Comment> Comments;
}
