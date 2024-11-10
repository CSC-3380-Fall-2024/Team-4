package org.eru.managers;

import org.eru.models.account.JTICreationExpiration;
import org.eru.models.mongo.user.Client;
import org.eru.models.mongo.user.Resource;
import org.eru.models.mongo.user.User;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class TokenManager {
    public static String makeID() {
        return UUID.randomUUID().toString();
    }

    public static String compressString(String input) {
        try {
            Deflater deflater = new Deflater();
            deflater.setInput(input.getBytes("UTF-8"));
            deflater.finish();

            byte[] buffer = new byte[1024];
            int compressedDataLength;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while (!deflater.finished()) {
                    compressedDataLength = deflater.deflate(buffer);
                    outputStream.write(buffer, 0, compressedDataLength);
                }

                return Base64.getEncoder().encodeToString(outputStream.toByteArray());
            }
        } catch (Exception e) {
            System.out.println("Error compressing string: ");
            e.printStackTrace();
            return null;
        }
    }

    public static String decompressString(String base64String) {
        try {
            byte[] compressedData = Base64.getDecoder().decode(base64String);

            Inflater inflator = new Inflater();
            inflator.setInput(compressedData);

            byte[] buffer = new byte[1024];
            int decompressedDataLength;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while (!inflator.finished()) {
                    decompressedDataLength = inflator.inflate(buffer);
                    outputStream.write(buffer, 0, decompressedDataLength);
                }

                return outputStream.toString("UTF-8");
            }
        } catch (Exception e) {
            System.out.println("Error decompressing string: ");
            e.printStackTrace();
            return null;
        }
    }

    public static String createClient(Client clientInfo, String grantType, String ip, int expiresIn) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
        Instant now = Instant.now();
        Instant ExpiresAt = now.plus(expiresIn, ChronoUnit.HOURS);

        Map<String, String> payload = new HashMap<>();
        StringBuilder permissionsBuilder = new StringBuilder();

        for (Resource resource : clientInfo.Perms.Client) {
            permissionsBuilder.append(resource.Resource).append("=").append(resource.Action).append(",");
        }

        String permissions = permissionsBuilder.substring(0, permissionsBuilder.length() - 1);

        String jti = makeID().replace("-", "");

        payload.put("p", compressString(permissions));
        payload.put("clsvc", clientInfo.ClientService);
        payload.put("t", "s");
        payload.put("mver", Boolean.toString(false));
        payload.put("clid", clientInfo.ClientId);
        payload.put("ic", Boolean.toString(clientInfo.internal));
        payload.put("am", grantType);
        payload.put("jti", jti);
        payload.put("pfpid", clientInfo.ClientService);

        JTICreationExpiration jce = new JTICreationExpiration(jti, dtf.format(now), expiresIn, clientInfo.ClientId, clientInfo.ClientService, clientInfo.Perms.Client, "AuthMethodPlaceholder");
        String clientToken = JwtGenerator.getInstance().generateJwt(payload, ExpiresAt);

        MongoDBManager.getInstance().pushClientToken(ip, jce);

        return "eg1~" + clientToken;
    }

    public static String createAccess(User user, Client clientInfo, String grantType, String deviceId, int expiresIn) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("UTC"));
        Instant now = Instant.now();
        Instant ExpiresAt = now.plus(expiresIn, ChronoUnit.HOURS);

        Map<String, String> payload = new HashMap<>();
        StringBuilder permissionsBuilder = new StringBuilder();

        for (Resource resource : clientInfo.Perms.Account) {
            permissionsBuilder.append(resource.Resource).append("=").append(resource.Action).append(",");
        }

        String permissions = permissionsBuilder.substring(0, permissionsBuilder.length() - 1);

        String jti = makeID().replace("-", "");

        payload.put("app", clientInfo.ClientService);
        payload.put("sub", user.AccountId);
        payload.put("dvid", deviceId);
        payload.put("mver", Boolean.toString(false));
        payload.put("clid", clientInfo.ClientId);
        payload.put("dn", user.DisplayName);
        payload.put("am", grantType);
        payload.put("pfpid", clientInfo.ClientService);
        payload.put("p", compressString(permissions));
        payload.put("iai", user.AccountId);
        payload.put("sec", Integer.toString(1));
        payload.put("acr", "urn:epic:loa:aal1");
        payload.put("clsvc", clientInfo.ClientService);
        payload.put("t", "s");
        payload.put("auth_time", Long.toString(now.getEpochSecond()));
        payload.put("ic", Boolean.toString(clientInfo.internal));
        payload.put("exp", Long.toString(ExpiresAt.getEpochSecond()));
        payload.put("iat", Long.toString(now.getEpochSecond()));
        payload.put("jti", jti);

        JTICreationExpiration jce = new JTICreationExpiration(jti, dtf.format(now), expiresIn, user.AccountId, deviceId, clientInfo.ClientId, clientInfo.ClientService, clientInfo.Perms.Account, "AuthMethodPlaceholder");
        String accessToken = JwtGenerator.getInstance().generateJwt(payload, ExpiresAt);

        MongoDBManager.getInstance().pushAccessToken(user.AccountId, jce);

        return "eg1~" + accessToken;
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
                "hours_expire", Integer.toString(expiresIn),
                "exp", Long.toString(ExpiresAt.getEpochSecond())
        );

        String refreshToken = JwtGenerator.getInstance().generateJwt(payload, ExpiresAt);
        MongoDBManager.getInstance().pushRefreshToken(user.AccountId, refreshToken);

        return refreshToken;
    }
}
