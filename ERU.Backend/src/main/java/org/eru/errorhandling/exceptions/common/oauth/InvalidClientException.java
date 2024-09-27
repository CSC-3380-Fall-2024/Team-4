package org.eru.errorhandling.exceptions.common.oauth;

import org.eru.errorhandling.BaseException;

public class InvalidClientException extends BaseException {
    public InvalidClientException() {
        super("errors.com.epicgames.common.oauth.invalid_client", "It appears that your Authorization header may be invalid or not present, please verify that you are sending the correct headers.", 400, 1011);
    }
}
