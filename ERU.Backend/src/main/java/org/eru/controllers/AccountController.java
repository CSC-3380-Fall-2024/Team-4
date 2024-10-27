package org.eru.controllers;

import org.eru.Variables;
import org.eru.errorhandling.exceptions.account.*;
import org.eru.errorhandling.exceptions.common.oauth.InvalidClientException;
import org.eru.errorhandling.exceptions.common.oauth.InvalidRequestException;
import org.eru.errorhandling.exceptions.common.oauth.UnsupportedGrantType;
import org.eru.managers.CryptoManager;
import org.eru.managers.JwtGenerator;
import org.eru.managers.MongoDBManager;
import org.eru.managers.TokenManager;
import org.eru.models.account.Account;
import org.eru.models.account.OAuthToken;
import org.eru.models.mongo.IPModel;
import org.eru.models.mongo.Token;
import org.eru.models.mongo.user.User;
import org.eru.models.websocket.Client;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
public class AccountController {

    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/account/api/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Account> createAccount(@RequestParam Map<String, String> paramMap, @RequestHeader Map<String, String> headers) throws InvalidClientException, EmailAlreadyTakenException, UsernameAlreadyTakenException, PasswordEmptyException {
        String email = paramMap.get("email");
        String username = paramMap.get("username");
        String name = paramMap.get("name");
        String password = paramMap.get("password");

        String clientId;
        String deviceId;

        // TODO: Use this with the rest of the endpoints to determine if it's coming from the client
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

        Account account = new Account();

        if (email != null && !email.isEmpty()) {
            // TODO: Send a confirmation email
            account.Email = email;

            if (MongoDBManager.getInstance().getAccountByEmail(email) != null) {
                throw new EmailAlreadyTakenException();
            }
        }

        if (username != null && !username.isEmpty()) {
            account.DisplayName = username;

            if (MongoDBManager.getInstance().getAccountByUsername(username) != null) {
                throw new UsernameAlreadyTakenException();
            }
        }

        if (name != null && !name.isEmpty()) {
            account.Name = name;
        }

        if (password != null && !password.isEmpty()) {
            account.Password = password;
        }
        else {
            throw new PasswordEmptyException();
        }

        account.Id = UUID.randomUUID().toString();
        while (MongoDBManager.getInstance().getAccountByAccountId(account.Id) != null) {
            account.Id = UUID.randomUUID().toString();
        }

        MongoDBManager.getInstance().pushAccount(account);

        return ResponseEntity.ok(account);
    }


    // TODO: Rewrite everything below
    @PostMapping(value = "/account/api/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<OAuthToken> token(@RequestParam Map<String, String> paramMap, @RequestHeader Map<String, String> headers)
            throws InvalidClientException, InvalidRequestException, InvalidAccountCredentials, UnsupportedGrantType, InvalidRefreshTokenException, AccountNotActiveException {
        if (paramMap == null) {
            throw new InvalidRequestException("Param map");
        }

        String grant_type = paramMap.get("grant_type");
        String email = paramMap.get("username");
        String password = paramMap.get("password");
        String refresh_token = paramMap.get("refresh_token");
        String exchange_code = paramMap.get("exchange_code");

        String clientId;
        User user = null;

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

        switch (grant_type) {
            case "client_credentials" -> {
                String ip = request.getRemoteAddr();
                IPModel clientToken = MongoDBManager.getInstance().getClientTokenByIp(ip);
                if (clientToken.Token != null && !clientToken.Token.isEmpty()) {
                    MongoDBManager.getInstance().removeClientTokenByIp(ip);
                }

                String token = TokenManager.createClient(clientId, grant_type, ip, 4);
                DecodedJWT decodedClientToken = JwtGenerator.getInstance().decodeJwt(token);

                return ResponseEntity.ok(new OAuthToken(token, decodedClientToken, clientId));
            }
            case "password" -> {
                if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                    throw new InvalidRequestException("Username/password");
                }

                //user = MongoDBManager.getInstance().getUserByEmail(email);

                if (user == null || user.Email == null || !user.Email.equals(email)) {
                    throw new InvalidAccountCredentials();
                }
                else {
                    if (user.Password == null || !user.Password.equals(CryptoManager.HashSha256(password))) {
                        throw new InvalidAccountCredentials();
                    }
                }
            }
            case "refresh_token" -> {
                if (refresh_token == null || refresh_token.isEmpty()) {
                    throw new InvalidRequestException("Refresh token");
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

                //user = MongoDBManager.getInstance().getUserByAccountId(token.AccountId);
            }
            case "exchange_code" -> {
                if (exchange_code == null || exchange_code.isEmpty()) {
                    throw new InvalidRequestException("Exchange code");
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

        Token accessIndex = MongoDBManager.getInstance().getAccessTokenByAccountId(user.AccountId);
        if (accessIndex != null && accessIndex.Token != null && !accessIndex.Token.isEmpty()) {
            MongoDBManager.getInstance().removeAccessTokenByAccountId(accessIndex.AccountId);

            User finalUser = user;
            Client xmppClient = Variables.WSConnection.getClients().stream().filter(c -> c.getAccountId().equals(finalUser.AccountId)).findFirst().orElse(null);
            if (xmppClient != null) {
                xmppClient.close();
            }
        }

        String deviceId = TokenManager.makeID().replace("-", "");
        String accessToken = TokenManager.createAccess(user, clientId, grant_type, deviceId, 8);
        String refreshToken = TokenManager.createRefresh(user, clientId, grant_type, deviceId, 24);

        DecodedJWT decodedAccess = JwtGenerator.getInstance().decodeJwt(accessToken);
        DecodedJWT decodedRefresh = JwtGenerator.getInstance().decodeJwt(refreshToken);

        //MongoDBManager.getInstance().updateUserByAccountId(user);

        return ResponseEntity.ok(new OAuthToken(accessToken, refreshToken, decodedAccess, decodedRefresh, user, clientId, deviceId));
    }

    @DeleteMapping("/account/api/oauth/sessions/kill")
    public ResponseEntity kill() {
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/account/api/oauth/sessions/kill/{token}")
    public ResponseEntity kill(@PathVariable String token) {

        Token object = MongoDBManager.getInstance().getAccessTokenByToken(token);
        if (object != null && object.AccountId != null && !object.AccountId.isEmpty()) {
            MongoDBManager.getInstance().removeAccessTokenByAccountId(object.AccountId);

            Token finalUser = object;
            Client xmppClient = Variables.WSConnection.getClients().stream().filter(c -> c.getAccountId().equals(finalUser.AccountId)).findFirst().orElse(null);
            if (xmppClient != null) {
                xmppClient.close();
            }

            MongoDBManager.getInstance().removeRefreshTokenByAccountId(object.AccountId);
        }

        MongoDBManager.getInstance().removeClientTokenByToken(token);

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/auth/v1/oauth/token")
    public ResponseEntity<OAuthToken> token() {
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
    public ResponseEntity<OAuthToken> tokenv2(@RequestParam Map<String, String> paramMap, @RequestHeader Map<String, String> headers)
            throws InvalidRefreshTokenException, InvalidClientException {
        String clientId;

        try {
            String[] auth = CryptoManager.DecodeBase64(headers.get("authorization").split(" ")[1]).split(":");

            if (auth.length != 2) {
                throw new InvalidClientException();
            }

            clientId = auth[0];
        } catch (Exception e) {
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
        } catch (Exception e) {
            if (refreshToken != null && refreshToken.AccountId != null && !refreshToken.AccountId.isEmpty()) {
                MongoDBManager.getInstance().removeRefreshTokenByToken(refresh_token);
            }

            throw new InvalidRefreshTokenException(refresh_token);
        }

        //User user = MongoDBManager.getInstance().getUserByAccountId(refreshToken.AccountId);

        return ResponseEntity.ok(new OAuthToken() {
            {
                Scope = new String[]{"basic_profile", "friends_list", "openid", "presence"};
                TokenType = "bearer";
                AccessToken = "ERUAccessToken";
                RefreshToken = "ERURefreshToken";
                IdToken = "ERUIdToken";
                ExpiresIn = 7200;
                ExpiresAt = "9999-12-31T23:59:59.999Z";
                RefreshExpires = 28800;
                RefreshExpiresAt = "9999-12-31T23:59:59.999Z";
                AccountId = "ACCOUNTID";
                ClientId = clientId;
                ApplicationId = "ERUApplicationId";
                SelectedAccountId = "ACCOUNTID";
                MergedAccounts = new String[0];
            }
        });
    }
}
