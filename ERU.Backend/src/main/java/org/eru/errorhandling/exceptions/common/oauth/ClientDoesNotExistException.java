package org.eru.errorhandling.exceptions.common.oauth;

import org.eru.errorhandling.BaseException;

public class ClientDoesNotExistException extends BaseException {
    public ClientDoesNotExistException() {
        super("errors.org.eru.account.invalid_client_credentials", "Sorry the client credentials you are using are invalid", 400, 18033);
    }
}
