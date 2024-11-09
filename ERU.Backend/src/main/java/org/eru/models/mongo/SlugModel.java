package org.eru.models.mongo;

import org.eru.enums.SACType;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class SlugModel {
    private ObjectId id;

    @BsonProperty("slug")
    public String Slug;

    @BsonProperty("discordId")
    public String DiscordId;

    @BsonProperty("type")
    public int Type;

    public SlugModel(String slug, String discordId, SACType type) {
        Slug = slug;
        DiscordId = discordId;
        Type = type.ordinal();
    }
}
