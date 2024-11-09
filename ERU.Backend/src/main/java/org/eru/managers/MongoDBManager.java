package org.eru.managers;

import org.eru.enums.SACType;
import org.eru.models.account.JTICreationExpiration;
import org.eru.models.mongo.IPModel;
import org.eru.models.mongo.SlugModel;
import org.eru.models.mongo.Token;
import org.eru.models.mongo.user.Client;
import org.eru.models.mongo.user.User;
import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


public class MongoDBManager {
    private static MongoDBManager INSTANCE;
    private final String CONNECTION_STRING = "mongodb+srv://Tamely:Tamely@apidata.xkthfib.mongodb.net/?retryWrites=true&w=majority";
    public static MongoDBManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MongoDBManager();
        }
        return INSTANCE;
    }

    private MongoClient DATABASE_CLIENT = null;
    private MongoDatabase USER_DATABASE = null;
    private MongoDatabase CLIENT_DATABASE = null;

    public MongoDBManager() {
        ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();

        DATABASE_CLIENT = MongoClients.create(clientSettings);
        USER_DATABASE = DATABASE_CLIENT.getDatabase("UserData");
        CLIENT_DATABASE = DATABASE_CLIENT.getDatabase("ClientData");
    }

    public User getUserByEmail(String email) {
        MongoCollection<User> collection = USER_DATABASE.getCollection("Accounts", User.class);
        if (collection.countDocuments(eq("email", email)) == 0) {
            return new User();
        }

        return collection.find(eq("email", email)).first();
    }

    public User getUserByAccountId(String accountId) {
        MongoCollection<User> collection = USER_DATABASE.getCollection("Accounts", User.class);
        if (collection.countDocuments(eq("accountId", accountId)) == 0) {
            return null;
        }

        return collection.find(eq("accountId", accountId)).first();
    }

    public void updateUserByAccountId(User user) {
        MongoCollection<User> collection = USER_DATABASE.getCollection("Accounts", User.class);
        if (collection.countDocuments(eq("accountId", user.AccountId)) == 0) {
            return;
        }

        collection.replaceOne(eq("accountId", user.AccountId), user);
    }

    public void removeUserByAccountId(String accountId) {
        MongoCollection<User> collection = USER_DATABASE.getCollection("Accounts", User.class);
        if (collection.countDocuments(eq("accountId", accountId)) == 0) {
            return;
        }

        collection.deleteOne(eq("accountId", accountId));
    }

    public void addUser(User user) {
        MongoCollection<User> collection = USER_DATABASE.getCollection("Accounts", User.class);
        collection.insertOne(user);
    }

    public void pushRefreshToken(String accountId, String refreshToken) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("RefreshTokens", Token.class);
        collection.insertOne(new Token(accountId, "eg1~" + refreshToken));
    }

    public Token getRefreshTokenByAccountId(String accountId) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("RefreshTokens", Token.class);
        if (collection.countDocuments(eq("accountId", accountId)) == 0) {
            return new Token();
        }

        return collection.find(eq("accountId", accountId)).first();
    }

    public Token getRefreshTokenByToken(String token) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("RefreshTokens", Token.class);
        if (collection.countDocuments(eq("token", token)) == 0) {
            return new Token();
        }

        return collection.find(eq("token", token)).first();
    }

    public void removeRefreshTokenByToken(String token) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("RefreshTokens", Token.class);
        collection.deleteOne(eq("token", token));
    }

    public void removeRefreshTokenByAccountId(String accountId) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("RefreshTokens", Token.class);
        collection.deleteOne(eq("accountId", accountId));
    }

    public void pushAccessToken(String accountId, JTICreationExpiration accessToken) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("AccessTokens", IPModel.class);
        collection.insertOne(new IPModel(accountId, accessToken));
    }

    public IPModel getAccessTokenByAccountId(String accountId) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("AccessTokens", IPModel.class);
        if (collection.countDocuments(eq("ip", accountId)) == 0) {
            return new IPModel();
        }

        return collection.find(eq("ip", accountId)).first();
    }

    public void removeAccessTokenByAccountId(String accountId) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("AccessTokens", IPModel.class);
        collection.deleteOne(eq("ip", accountId));
    }

    public void removeAccessTokenByJTI(String JTI) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("AccessTokens", IPModel.class);

        for (IPModel token : collection.find()) {
            if (token == null || token.Token == null) continue;
            if (token.Token.JTI.equals(JTI)) {
                collection.deleteOne(eq("ip", token.Ip));
                return;
            }
        }
    }

    public IPModel getAccessTokenByJTI(String JTI) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("AccessTokens", IPModel.class);

        for (IPModel token : collection.find()) {
            if (token == null || token.Token == null) continue;
            if (token.Token.JTI.equals(JTI)) return token;
        }

        return new IPModel();
    }

    public void pushClientToken(String ip, JTICreationExpiration clientToken) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("ClientTokens", IPModel.class);
        collection.insertOne(new IPModel(ip, clientToken));
    }

    public IPModel getClientTokenByIp(String ip) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("ClientTokens", IPModel.class);
        if (collection.countDocuments(eq("ip", ip)) == 0) {
            return new IPModel();
        }

        return collection.find(eq("ip", ip)).first();
    }

    public IPModel getClientTokenByJTI(String JTI) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("ClientTokens", IPModel.class);

        for (IPModel token : collection.find()) {
            if (token == null || token.Token == null) continue;
            if (token.Token.JTI.equals(JTI)) return token;
        }

        return new IPModel();
    }

    public void removeClientTokenByIp(String ip) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("ClientTokens", IPModel.class);
        collection.deleteOne(eq("ip", ip));
    }

    public void removeClientTokenByJTI(String JTI) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("ClientTokens", IPModel.class);

        for (IPModel token : collection.find()) {
            if (token == null || token.Token == null) continue;
            if (token.Token.JTI.equals(JTI)) {
                collection.deleteOne(eq("ip", token.Ip));
                return;
            }
        }
    }

    public void pushSlug(String slug, String accountId, SACType type) {
        MongoCollection<SlugModel> collection = USER_DATABASE.getCollection("CreatorSlugs", SlugModel.class);
        collection.insertOne(new SlugModel(slug, accountId, type));
    }

    public void deleteSlugBySlug(String slug) {
        MongoCollection<SlugModel> collection = USER_DATABASE.getCollection("CreatorSlugs", SlugModel.class);
        collection.deleteMany(eq("slug", slug));
    }

    public void deleteSlugByDiscordId(String discordId) {
        MongoCollection<SlugModel> collection = USER_DATABASE.getCollection("CreatorSlugs", SlugModel.class);
        collection.deleteMany(eq("discordId", discordId));
    }

    public void deleteSlugByType(SACType type) {
        MongoCollection<SlugModel> collection = USER_DATABASE.getCollection("CreatorSlugs", SlugModel.class);
        collection.deleteMany(eq("type", type));
    }

    public boolean slugExists(String slug) {
        MongoCollection<SlugModel> collection = USER_DATABASE.getCollection("CreatorSlugs", SlugModel.class);
        return collection.countDocuments(eq("slug", slug)) != 0;
    }

    public boolean slugExistsByDiscordId(String discordId) {
        MongoCollection<SlugModel> collection = USER_DATABASE.getCollection("CreatorSlugs", SlugModel.class);
        return collection.countDocuments(eq("discordId", discordId)) != 0;
    }

    public SlugModel getSlugByDiscordId(String discordId) {
        MongoCollection<SlugModel> collection = USER_DATABASE.getCollection("ClientTokens", SlugModel.class);
        if (collection.countDocuments(eq("discordId", discordId)) == 0) {
            return null;
        }

        return collection.find(eq("discordId", discordId)).first();
    }
    public Client getClientById(String clientId) {
        MongoCollection<Client> collection = CLIENT_DATABASE.getCollection("Clients", Client.class);
        if (collection.countDocuments(eq("_id", clientId)) == 0) {
            return null;
        }

        return collection.find(eq("_id", clientId)).first();
    }
}
