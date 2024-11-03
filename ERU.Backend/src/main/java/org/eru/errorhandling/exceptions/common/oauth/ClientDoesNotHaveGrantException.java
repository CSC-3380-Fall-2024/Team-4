package org.eru.errorhandling.exceptions.common.oauth;

import org.eru.errorhandling.BaseException;

public class ClientDoesNotHaveGrantException extends BaseException {
    public ClientDoesNotHaveGrantException(String grantType) {
        super("errors.com.epicgames.common.oauth.unauthorized_client", "Sorry your client is not allowed to use the grant type " + grantType, 400, 1015);
    }
}
