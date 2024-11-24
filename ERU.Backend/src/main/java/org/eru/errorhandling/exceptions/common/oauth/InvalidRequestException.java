package org.eru.errorhandling.exceptions.common.oauth;

import org.eru.errorhandling.BaseException;

public class InvalidRequestException extends BaseException {
    public InvalidRequestException(String missing) {
        super("errors.org.eru.common.oauth.invalid_request", missing + " is required.", 400, 1013);
    }
}
