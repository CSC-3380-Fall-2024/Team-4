package org.eru.models.mongo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.eru.models.mongo.user.actions.Comment;
import org.eru.models.mongo.user.actions.Like;

import java.util.List;

public class Post {
    // encoded with b64
    @JsonProperty("content")
    public String Content;

    @JsonProperty("caption")
    public String Caption;

    @JsonProperty("likes")
    public List<Like> Likes;

    @JsonProperty("comments")
    public List<Comment> Comments;

    @BsonIgnore
    @JsonProperty("owning_user")
    public String OwningUser;

    @BsonIgnore
    @JsonProperty("owning_picture")
    public String OwningPicture;

    @JsonProperty("identifier")
    public String Identifier;
}