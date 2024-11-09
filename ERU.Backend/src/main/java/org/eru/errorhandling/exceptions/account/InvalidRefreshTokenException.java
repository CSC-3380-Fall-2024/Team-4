package org.eru.errorhandling.exceptions.account;

import org.eru.errorhandling.BaseException;

public class InvalidRefreshTokenException extends BaseException {
    public InvalidRefreshTokenException(String refreshToken) {
        super("errors.com.epicgames.account.auth_token.invalid_refresh_token", "Sorry the refresh token " + refreshToken + " is invalid", 400, 18036);
    }
}
