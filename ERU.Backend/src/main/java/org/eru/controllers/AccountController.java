package org.eru.controllers;

import org.eru.errorhandling.exceptions.account.AccountNotActiveException;
import org.eru.errorhandling.exceptions.account.AccountNotFoundException;
import org.eru.errorhandling.exceptions.account.InvalidAccountCredentials;
import org.eru.errorhandling.exceptions.account.InvalidRefreshTokenException;
import org.eru.errorhandling.exceptions.common.oauth.*;
import org.eru.managers.CryptoManager;
import org.eru.managers.JwtGenerator;
import org.eru.managers.MongoDBManager;
import org.eru.managers.TokenManager;
import org.eru.models.account.Account;
import org.eru.models.account.OAuthToken;
import org.eru.models.mongo.IPModel;
import org.eru.models.mongo.Token;
import org.eru.models.mongo.user.User;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class AccountController {

    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/account/api/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<OAuthToken> token(@RequestParam Map<String, String> paramMap, @RequestHeader Map<String, String> headers)
            throws InvalidClientException, InvalidRequestException, InvalidAccountCredentials, UnsupportedGrantType, InvalidRefreshTokenException, AccountNotActiveException, AccountNotFoundException {
        if (paramMap == null) {
            throw new InvalidRequestException("Param map");
        }

        String grant_type = paramMap.get("grant_type");
        String email = paramMap.get("username");
        String password = paramMap.get("password");
        String refresh_token = paramMap.get("refresh_token");
        String exchange_code = paramMap.get("exchange_code");
        String token_type = paramMap.get("token_type");
        boolean includePerms = paramMap.get("includePerms") != null && paramMap.get("includePerms").equals("true");

        String clientId;
        String clientSecret;
        org.eru.models.mongo.user.Client clientInfo;
        User user = null;

        try {
            String[] auth = CryptoManager.DecodeBase64(headers.get("authorization").split(" ")[1]).split(":");

            if (auth.length != 2) {
                throw new InvalidClientException();
            }

            clientId = auth[0];
            clientSecret = auth[1];
            clientInfo = MongoDBManager.getInstance().getClientById(clientId);

            if (clientInfo == null) {
                throw new ClientDoesNotExistException();
            }
            if (!Objects.equals(clientSecret, clientInfo.ClientSecret)) {
                throw new ClientDoesNotExistException();
            }

            if (!clientInfo.GrantTypes.contains(grant_type)) {
                throw new ClientDoesNotHaveGrantException(grant_type);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }


        switch (grant_type) {
            case "client_credentials" -> {
                String ip = request.getRemoteAddr();
                IPModel clientToken = MongoDBManager.getInstance().getClientTokenByIp(ip);
                if (clientToken.Token != null) {
                    MongoDBManager.getInstance().removeClientTokenByIp(ip);
                }

                String token = TokenManager.createClient(clientInfo, grant_type, ip, 4);

                return ResponseEntity.ok(new OAuthToken(token, token_type, clientId, clientInfo.ClientService, clientInfo.internal));
            }
            case "password" -> {
                if (email == null || email.isEmpty()) {
                    throw new InvalidRequestException("username");
                }
                else if (password == null || password.isEmpty()) {
                    throw new InvalidRequestException("password");
                }
                user = MongoDBManager.getInstance().getUserByEmail(email);
                if (user == null) {
                    throw new AccountNotFoundException(email);
                }

                if (user.Email == null || !user.Email.equals(email)) {
                    throw new InvalidAccountCredentials();
                }
                else {
                    if (user.Password == null || !user.Password.equals(password)) {
                        throw new InvalidAccountCredentials();
                    }
                }

                String deviceId = TokenManager.makeID().replace("-", "");
                String accessToken = TokenManager.createAccess(user, clientInfo, grant_type, deviceId, 8);
                String refreshToken = TokenManager.createRefresh(user, clientId, grant_type, deviceId, 24);

                DecodedJWT decodedAccess = JwtGenerator.getInstance().decodeJwt(accessToken.replace("eg1~", ""));
                DecodedJWT decodedRefresh = JwtGenerator.getInstance().decodeJwt(refreshToken);

                return ResponseEntity.ok(new OAuthToken(accessToken, refreshToken, decodedAccess, decodedRefresh, user, clientId, deviceId));
            }
            case "refresh_token" -> {
                if (refresh_token == null || refresh_token.isEmpty()) {
                    throw new InvalidRequestException("refresh_token");
                }

                Token token = MongoDBManager.getInstance().getRefreshTokenByToken(refresh_token);
                try {
                    if (token == null || token.AccountId == null || token.AccountId.isEmpty()) {
                        throw new Exception("Invalid refresh token");
                    }

                    DecodedJWT decodedRefreshToken = JwtGenerator.getInstance().decodeJwt(token.Token.replace("eg1~", ""));

                    String creationDate = decodedRefreshToken.getClaim("creation_date").asString();
                    int expirationHours = Integer.parseInt(decodedRefreshToken.getClaim("hours_expire").asString());

                    Instant creationDateInstant = Instant.parse(creationDate);
                    Instant expireInstant = creationDateInstant.plusSeconds(expirationHours * 3600L);

                    if ((expireInstant.getEpochSecond() - Instant.now().getEpochSecond() <= 0)) {
                        throw new Exception("Expired refresh token");
                    }
                }
                catch (Exception e) {
                    if (token != null && token.AccountId != null && !token.AccountId.isEmpty()) {
                        MongoDBManager.getInstance().removeRefreshTokenByToken(refresh_token);
                    }

                    throw new InvalidRefreshTokenException(refresh_token);
                }

                user = MongoDBManager.getInstance().getUserByAccountId(token.AccountId);
            }
            case "exchange_code" -> {
                if (exchange_code == null || exchange_code.isEmpty()) {
                    throw new InvalidRequestException("exchange_code");
                }
            }
            default -> {
                throw new UnsupportedGrantType(grant_type);
            }
        }

        if (user == null) {
            throw new InvalidAccountCredentials();
        }

        if (user.Banned) {
            throw new AccountNotActiveException();
        }

        Token refreshIndex = MongoDBManager.getInstance().getRefreshTokenByAccountId(user.AccountId);
        if (refreshIndex != null && refreshIndex.Token != null && !refreshIndex.Token.isEmpty()) {
            MongoDBManager.getInstance().removeRefreshTokenByToken(refreshIndex.Token);
        }

        IPModel accessIndex = MongoDBManager.getInstance().getAccessTokenByAccountId(user.AccountId);
        if (accessIndex != null && accessIndex.Ip != null && !accessIndex.Ip.isEmpty()) {
            MongoDBManager.getInstance().removeAccessTokenByAccountId(user.AccountId);
        }

        String deviceId = TokenManager.makeID().replace("-", "");
        String accessToken = TokenManager.createAccess(user, clientInfo, grant_type, deviceId, 8);
        String refreshToken = TokenManager.createRefresh(user, clientId, grant_type, deviceId, 24);

        DecodedJWT decodedAccess = JwtGenerator.getInstance().decodeJwt(accessToken.replace("eg1~", ""));
        DecodedJWT decodedRefresh = JwtGenerator.getInstance().decodeJwt(refreshToken);

        return ResponseEntity.ok(new OAuthToken(accessToken, refreshToken, decodedAccess, decodedRefresh, user, clientId, deviceId));
    }

    @DeleteMapping("/account/api/oauth/sessions/kill")
    public ResponseEntity kill() {
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/account/api/oauth/sessions/kill/{token}")
    public ResponseEntity kill(@PathVariable String token) {
        boolean offlineToken = token.startsWith("eg1~");
        if (offlineToken) {
            DecodedJWT decodedToken = JwtGenerator.getInstance().decodeJwt(token.replace("eg1~", ""));
            String jti = decodedToken.getClaim("jti").asString();

            IPModel object = MongoDBManager.getInstance().getAccessTokenByJTI(jti);
            if (object != null && object.Ip != null && !object.Ip.isEmpty()) {
                MongoDBManager.getInstance().removeAccessTokenByJTI(jti);
                MongoDBManager.getInstance().removeRefreshTokenByAccountId(object.Ip);
            }
            else {
                object = MongoDBManager.getInstance().getClientTokenByJTI(jti);
                if (object != null && object.Ip != null && !object.Ip.isEmpty()) {
                    MongoDBManager.getInstance().removeClientTokenByJTI(jti);
                }
            }
        }
        else {
            System.out.println("We don't support opaque tokens just yet!");
        }

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/auth/v1/oauth/token")
    public ResponseEntity<OAuthToken> eosoauth() {
        return ResponseEntity.ok(new OAuthToken() {
            {
                AccessToken = "ERUAccessToken";
                TokenType = "bearer";
                ExpiresAt = "9999-12-31T23:59:59.999Z";
                Features = new String[] { "Connect", "Ecom" };
                OrganizationId = "ERUOrganizationId";
                ProductId = "prod-eru";
                SandboxId = "eru";
                DeploymentId = "ERUDeploymentId";
                ExpiresIn = 3599;
            }
        });
    }

    @PostMapping("/epic/oauth/v2/token")
    public ResponseEntity<OAuthToken> easoauth(@RequestParam Map<String, String> paramMap, @RequestHeader Map<String, String> headers)
            throws InvalidRefreshTokenException, InvalidClientException {
        String clientId;

        try {
            String[] auth = CryptoManager.DecodeBase64(headers.get("authorization").split(" ")[1]).split(":");

            if (auth.length != 2) {
                throw new InvalidClientException();
            }

            clientId = auth[0];
        }
        catch (Exception e) {
            throw new InvalidClientException();
        }

        String refresh_token = paramMap.get("refresh_token");

        Token refreshToken = MongoDBManager.getInstance().getRefreshTokenByToken(refresh_token);
        try {
            if (refreshToken == null || refreshToken.AccountId == null || refreshToken.AccountId.isEmpty()) {
                throw new Exception("Invalid refresh token");
            }

            DecodedJWT decodedRefreshToken = JwtGenerator.getInstance().decodeJwt(refreshToken.Token.replace("eg1~", ""));

            String creationDate = decodedRefreshToken.getClaim("creation_date").asString();
            int expirationHours = Integer.parseInt(decodedRefreshToken.getClaim("hours_expire").asString());

            Instant creationDateInstant = Instant.parse(creationDate);
            Instant expireInstant = creationDateInstant.plusSeconds(expirationHours * 3600L);

            if ((expireInstant.getEpochSecond() - Instant.now().getEpochSecond() <= 0)) {
                throw new Exception("Expired refresh token");
            }
        }
        catch(Exception e) {
            if (refreshToken != null && refreshToken.AccountId != null && !refreshToken.AccountId.isEmpty()) {
                MongoDBManager.getInstance().removeRefreshTokenByToken(refresh_token);
            }

            throw new InvalidRefreshTokenException(refresh_token);
        }

        User user = MongoDBManager.getInstance().getUserByAccountId(refreshToken.AccountId);

        return ResponseEntity.ok(new OAuthToken() {
            {
                Scope = new String[] { "basic_profile", "friends_list", "openid", "presence" };
                TokenType = "bearer";
                AccessToken = "ERUAccessToken";
                RefreshToken = "ERURefreshToken";
                IdToken = "ERUIdToken";
                ExpiresIn = 7200;
                ExpiresAt = "9999-12-31T23:59:59.999Z";
                RefreshExpires = 28800;
                RefreshExpiresAt = "9999-12-31T23:59:59.999Z";
                AccountId = user.AccountId;
                ClientId = clientId;
                ApplicationId = "ERUApplicationId";
                SelectedAccountId = user.AccountId;
                MergedAccounts = new String[0];
            }
        });
    }

    @GetMapping("/account/api/public/account/{accountId}")
    public ResponseEntity<Account> getAccountLookupById(@PathVariable String accountId) {
        User user = MongoDBManager.getInstance().getUserByAccountId(accountId);
        return ResponseEntity.ok(new Account() {
            {
                Id = user.AccountId;
                DisplayName = user.DisplayName;
            }
        });
    }

    @GetMapping("/account/api/public/account/{accountId}/externalAuths")
    public ResponseEntity<ArrayList<String>> getExternalAuthsById(@PathVariable String accountId) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/account/api/oauth/verify")
    public ResponseEntity<OAuthToken> verify(@RequestHeader Map<String, String> headers) {
        String token = headers.get("authorization").replace("bearer ", "");
        org.eru.models.mongo.user.Client clientInfo;
        boolean offlineToken = token.startsWith("eg1~");
        if (offlineToken) {
            DecodedJWT decodedToken = JwtGenerator.getInstance().decodeJwt(token.replace("eg1~", ""));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
            String jti = decodedToken.getClaim("jti").asString();
            String clid = decodedToken.getClaim("clid").asString();
            clientInfo = MongoDBManager.getInstance().getClientById(clid);
            String am = decodedToken.getClaim("am").asString();
            String dvid = decodedToken.getClaim("dvid").asString();
            String sub = decodedToken.getClaim("sub").asString();

            User user = MongoDBManager.getInstance().getUserByAccountId(sub);

            String creationDate = decodedToken.getClaim("auth_time").asString();
            int expirationHours = Integer.parseInt(decodedToken.getClaim("exp").asString());

            Instant creationDateInstant = Instant.parse(creationDate);
            Instant expireInstant = creationDateInstant.plusSeconds(expirationHours * 3600L);
            return ResponseEntity.ok(new OAuthToken() {
                {
                    Token = token;
                    SessionId = jti;
                    TokenType = "bearer";
                    ClientId = clid;
                    InternalClient = clientInfo.internal;
                    ClientService = clientInfo.ClientService;
                    AccountId = user.AccountId;
                    ExpiresIn = (int) (expireInstant.getEpochSecond() - Instant.now().getEpochSecond());
                    ExpiresAt = dtf.format(expireInstant);
                    AuthMethod = am;
                    DisplayName = user.DisplayName;
                    App = clientInfo.ClientService;
                    InAppId = user.AccountId;
                    DeviceId = dvid;
                }
            });
        }
        return null;
    }

    @GetMapping("/account/api/public/account")
    public ResponseEntity<ArrayList<Account>> getAccounts(@RequestParam(value = "accountId", required = false) String[] accountIdArray)
    {
        ArrayList<Account> ret = new ArrayList<>();

        if (accountIdArray != null) {
            for (String id : accountIdArray) {
                if (ret.size() >= 100) break;

                User user = MongoDBManager.getInstance().getUserByAccountId(id);

                if (user != null && !user.AccountId.isEmpty()) {
                    ret.add(new Account() {
                        {
                            Id = user.AccountId;
                            DisplayName = user.DisplayName;
                        }
                    });
                }
            }
        }

        return ResponseEntity.ok(ret);
    }
}
