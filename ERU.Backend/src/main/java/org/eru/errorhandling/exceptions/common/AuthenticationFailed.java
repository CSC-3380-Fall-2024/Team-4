package org.eru.errorhandling.exceptions.common;

import org.eru.errorhandling.BaseException;

public class AuthenticationFailed extends BaseException {
    public AuthenticationFailed(String context) {
        super("errors.org.eru.common.authentication_failed", "Authentication failed for " + context, 400, 1032);
    }
}
