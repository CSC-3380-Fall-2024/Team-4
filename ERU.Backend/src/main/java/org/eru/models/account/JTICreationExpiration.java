package org.eru.models.account;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.eru.models.mongo.user.Resource;

import java.util.List;

public class JTICreationExpiration {
    @BsonProperty("jti")
    public String JTI;

    @BsonProperty("auth_time")
    public String AuthTime;

    @BsonProperty("expires_in")
    public int ExpiresIn;

    @BsonProperty("account_id")
    public String AccountID;

    @BsonProperty("device_id")
    public String DeviceID;

    @BsonProperty("client_id")
    public String ClientID;

    @BsonProperty("client_service")
    public String ClientService;

    @BsonProperty("perms")
    public List<Resource> Perms;

    @BsonProperty("auth_method")
    public String AuthMethod;

    public JTICreationExpiration(String jti, String authTime, int expiresIn, String clientId, String clientService, List<Resource> perms, String authMethod) {
        JTI = jti;
        AuthTime = authTime;
        ExpiresIn = expiresIn;
        ClientID = clientId;
        ClientService = clientService;
        AuthMethod = authMethod;
        Perms = perms;
    }

    public JTICreationExpiration(String jti, String authTime, int expiresIn, String accountId, String deviceId, String clientId, String clientService, List<Resource> perms, String authMethod) {
        JTI = jti;
        AuthTime = authTime;
        ExpiresIn = expiresIn;
        AccountID = accountId;
        DeviceID = deviceId;
        ClientID = clientId;
        ClientService = clientService;
        AuthMethod = authMethod;
        Perms = perms;
    }

    public JTICreationExpiration() {}
}
