package org.eru.models.account;

import org.eru.managers.JwtGenerator;
import org.eru.managers.MongoDBManager;
import org.eru.models.mongo.user.Client;
import org.eru.models.mongo.user.User;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class OAuthToken {
    @JsonProperty("access_token")
    public String AccessToken;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("token")
    public String Token;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("session_id")
    public String SessionId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("auth_method")
    public String AuthMethod;

    @JsonProperty("expires_in")
    public int ExpiresIn;

    @JsonProperty("expires_at")
    public String ExpiresAt;

    @JsonProperty("token_type")
    public String TokenType = "bearer";

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("refresh_token")
    public String RefreshToken;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("id_token")
    public String IdToken;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("refresh_expires")
    public int RefreshExpires;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("refresh_expires_at")
    public String RefreshExpiresAt;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("account_id")
    public String AccountId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("acr")
    public String Acr;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("app")
    public String App;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("auth_time")
    public String AuthTime;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("client_id")
    public String ClientId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("internal_client")
    public boolean InternalClient;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("client_service")
    public String ClientService;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("scope")
    public String[] Scope;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("displayName")
    public String DisplayName;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("in_app_id")
    public String InAppId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("device_id")
    public String DeviceId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("deployment_id")
    public String DeploymentId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("sandbox_id")
    public String SandboxId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("product_id")
    public String ProductId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("organization_id")
    public String OrganizationId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("application_id")
    public String ApplicationId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("selected_account_id")
    public String SelectedAccountId;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("merged_accounts")
    public String[] MergedAccounts;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("features")
    public String[] Features;

    public OAuthToken(String token, DecodedJWT decoded, String clientId) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
        AccessToken = "eg1~" + token;

        String creationDate = decoded.getClaim("creation_date").asString();
        int expirationHours = Integer.parseInt(decoded.getClaim("hours_expire").asString());

        Instant creationDateInstant = Instant.parse(creationDate);
        Instant expireInstant = creationDateInstant.plusSeconds(expirationHours * 3600L);

        ExpiresIn = (int) (expireInstant.getEpochSecond() - Instant.now().getEpochSecond());
        ExpiresAt = dtf.format(expireInstant);

        ClientId = clientId;
    }

    public OAuthToken(String access, String refresh, DecodedJWT decodedAccess, DecodedJWT decodedRefresh, User user, String clientId, String deviceId) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
        Client clientInfo = MongoDBManager.getInstance().getClientById(clientId);
        AccessToken = access;

        long expiration = decodedAccess.getClaim("exp").asLong();
        Instant expireInstant = Instant.ofEpochSecond(expiration);

        ExpiresIn = (int) (expireInstant.getEpochSecond() - Instant.now().getEpochSecond());
        ExpiresAt = dtf.format(expireInstant);

        RefreshToken = "eg1~" + refresh;

        expiration = decodedRefresh.getClaim("exp").asLong();
        expireInstant = Instant.ofEpochSecond(expiration);

        RefreshExpires = (int) (expireInstant.getEpochSecond() - Instant.now().getEpochSecond());
        RefreshExpiresAt = dtf.format(expireInstant);

        AccountId = user.AccountId;
        ClientId = clientId;
        DisplayName = user.DisplayName;
        InAppId = user.AccountId;
        DeviceId = deviceId;

        InternalClient = clientInfo.internal;
        App = clientInfo.ClientService;
    }

    public OAuthToken() {}

    public OAuthToken(String accessToken, String accountId, String clientId, String deviceId, String displayName, String refreshToken) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));

        AccessToken = accessToken;
        AccountId = accountId;
        Acr = "urn:eru:loa:aal1";
        App = "prod-eru";
        AuthTime = dtf.format(Instant.now());
        ClientId = clientId;
        ClientService = "prod-eru";
        DeviceId = deviceId;
        DisplayName = displayName;
        ExpiresAt = dtf.format(Instant.now().plusSeconds(7200));
        ExpiresIn = 7200;
        InAppId = accountId;
        InternalClient = false;
        ProductId = "prod-eru";

        RefreshToken = refreshToken;

        //String creationDate = decodedRefresh.getClaim("creation_date").asString();
        //int expirationHours = Integer.parseInt(decodedRefresh.getClaim("hours_expire").asString());

        //Instant creationDateInstant = Instant.parse(creationDate);
        //Instant expireInstant = creationDateInstant.plusSeconds(expirationHours * 3600L);

        RefreshExpires = 0;
        RefreshExpiresAt = "";

        TokenType = "bearer";
    }

    public OAuthToken(String accessToken, String token_type, String clientId, String clientService, boolean internalClient) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));

        if (token_type != null && token_type.equals("eg1")) {
            AccessToken = accessToken;
        }
        else {
            DecodedJWT decodedJWT = JwtGenerator.getInstance().decodeJwt(accessToken.replace("eg1~", ""));
            AccessToken = decodedJWT.getClaim("jti").asString();
        }

        ClientId = clientId;
        ClientService = clientService;
        ExpiresAt = dtf.format(Instant.now().plusSeconds(14400));
        ExpiresIn = 14400;
        InternalClient = internalClient;
        ProductId = clientService;
        TokenType = "bearer";
    }
}