package org.eru.errorhandling.exceptions.common.oauth;

import org.eru.errorhandling.BaseException;

public class UnsupportedGrantType extends BaseException {
    public UnsupportedGrantType(String grantType) {
        super("errors.org.eru.common.oauth.unsupported_grant_type", "Unsupported grant type: " + grantType, 400, 1016);
    }
}
