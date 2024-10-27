package org.eru.managers;

import org.eru.models.account.Account;
import org.eru.models.mongo.IPModel;
import org.eru.models.mongo.Token;
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
    }

    public Account getAccountByEmail(String email) {
        MongoCollection<Account> collection = USER_DATABASE.getCollection("Users", Account.class);
        if (collection.countDocuments(eq("email", email)) == 0) {
            return null;
        }

        return collection.find(eq("email", email)).first();
    }

    public Account getAccountByUsername(String username) {
        MongoCollection<Account> collection = USER_DATABASE.getCollection("Users", Account.class);
        if (collection.countDocuments(eq("display_name", username)) == 0) {
            return null;
        }

        return collection.find(eq("display_name", username)).first();
    }

    public Account getAccountByAccountId(String accountId) {
        MongoCollection<Account> collection = USER_DATABASE.getCollection("Accounts", Account.class);
        if (collection.countDocuments(eq("account_id", accountId)) == 0) {
            return null;
        }

        return collection.find(eq("account_id", accountId)).first();
    }

    public void updateAccountByAccountId(Account user) {
        MongoCollection<Account> collection = USER_DATABASE.getCollection("Accounts", Account.class);
        if (collection.countDocuments(eq("account_id", user.Id)) == 0) {
            return;
        }

        collection.replaceOne(eq("account_id", user.Id), user);
    }

    public void pushAccount(Account user) {
        MongoCollection<Account> collection = USER_DATABASE.getCollection("Accounts", Account.class);
        if (collection.countDocuments(eq("account_id", user.Id)) != 0) {
            return;
        }

        collection.insertOne(user);
    }

    public void removeAccountByAccountId(String accountId) {
        MongoCollection<Account> collection = USER_DATABASE.getCollection("Accounts", Account.class);
        if (collection.countDocuments(eq("account_id", accountId)) == 0) {
            return;
        }

        collection.deleteOne(eq("account_id", accountId));
    }

    public void addAccount(Account user) {
        MongoCollection<Account> collection = USER_DATABASE.getCollection("Accounts", Account.class);
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

    public void pushAccessToken(String accountId, String accessToken) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("AccessTokens", Token.class);
        collection.insertOne(new Token(accountId, "eg1~" + accessToken));
    }

    public Token getAccessTokenByAccountId(String accountId) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("AccessTokens", Token.class);
        if (collection.countDocuments(eq("accountId", accountId)) == 0) {
            return new Token();
        }

        return collection.find(eq("accountId", accountId)).first();
    }

    public void removeAccessTokenByAccountId(String accountId) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("AccessTokens", Token.class);
        collection.deleteOne(eq("accountId", accountId));
    }

    public void removeAccessTokenByToken(String token) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("AccessTokens", Token.class);
        collection.deleteOne(eq("token", token));
    }

    public Token getAccessTokenByToken(String token) {
        MongoCollection<Token> collection = USER_DATABASE.getCollection("AccessTokens", Token.class);
        if (collection.countDocuments(eq("token", token)) == 0) {
            return new Token();
        }

        return collection.find(eq("token", token)).first();
    }

    public void pushClientToken(String ip, String clientToken) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("ClientTokens", IPModel.class);
        collection.insertOne(new IPModel(ip, "eg1~" + clientToken));
    }

    public IPModel getClientTokenByIp(String ip) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("ClientTokens", IPModel.class);
        if (collection.countDocuments(eq("ip", ip)) == 0) {
            return new IPModel();
        }

        return collection.find(eq("ip", ip)).first();
    }

    public void removeClientTokenByIp(String ip) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("ClientTokens", IPModel.class);
        collection.deleteOne(eq("ip", ip));
    }

    public void removeClientTokenByToken(String token) {
        MongoCollection<IPModel> collection = USER_DATABASE.getCollection("ClientTokens", IPModel.class);
        collection.deleteOne(eq("token", token));
    }
}
