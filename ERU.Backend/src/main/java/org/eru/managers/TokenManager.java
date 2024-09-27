package org.eru.managers;

import org.eru.models.mongo.user.User;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenManager {
    public static String makeID() {
        return UUID.randomUUID().toString();
    }

    public static String createClient(String clientId, String grantType, String ip, int expiresIn) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
        Instant now = Instant.now();
        Instant ExpiresAt = now.plus(expiresIn, ChronoUnit.HOURS);

        Map<String, String> payload = new HashMap<>();

        payload.put("p", CryptoManager.EncodeBase64(makeID()));
        payload.put("clsvc", "eru");
        payload.put("t", "s");
        payload.put("mver", Boolean.toString(false));
        payload.put("clid", clientId);
        payload.put("ic", Boolean.toString(true));
        payload.put("am", grantType);
        payload.put("jti", makeID().replace("-", ""));
        payload.put("creation_date", dtf.format(now));
        payload.put("hours_expire", Integer.toString(expiresIn));

        String clientToken = JwtGenerator.getInstance().generateJwt(payload, ExpiresAt);
        MongoDBManager.getInstance().pushClientToken(ip, clientToken);

        return clientToken;
    }

    public static String createAccess(User user, String clientId, String grantType, String deviceId, int expiresIn) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
        Instant now = Instant.now();
        Instant ExpiresAt = now.plus(expiresIn, ChronoUnit.HOURS);

        Map<String, String> payload = new HashMap<>();

        payload.put("app", "eru");
        payload.put("sub", user.AccountId);
        payload.put("dvid", deviceId);
        payload.put("mver", Boolean.toString(false));
        payload.put("clid", clientId);
        payload.put("am", grantType);
        payload.put("p", CryptoManager.EncodeBase64(makeID()));
        payload.put("iai", user.AccountId);
        payload.put("sec", Integer.toString(1));
        payload.put("clsvc", "eru");
        payload.put("t", "s");
        payload.put("ic", Boolean.toString(true));
        payload.put("jti", makeID().replace("-", ""));
        payload.put("creation_date", dtf.format(now));
        payload.put("hours_expire", Integer.toString(expiresIn));

        String accessToken = JwtGenerator.getInstance().generateJwt(payload, ExpiresAt);
        MongoDBManager.getInstance().pushAccessToken(user.AccountId, accessToken);

        return accessToken;
    }
    public static String createRefresh(User user, String clientId, String grantType, String deviceId, int expiresIn) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
        Instant now = Instant.now();
        Instant ExpiresAt = now.plus(expiresIn, ChronoUnit.HOURS);

        Map<String, String> payload = Map.of(
                "sub", user.AccountId,
                "dvid", deviceId,
                "t", "r",
                "clid", clientId,
                "am", grantType,
                "jti", makeID().replace("-", ""),
                "creation_date", dtf.format(now),
                "hours_expire", Integer.toString(expiresIn)
        );

        String refreshToken = JwtGenerator.getInstance().generateJwt(payload, ExpiresAt);
        MongoDBManager.getInstance().pushRefreshToken(user.AccountId, refreshToken);

        return refreshToken;
    }
}
