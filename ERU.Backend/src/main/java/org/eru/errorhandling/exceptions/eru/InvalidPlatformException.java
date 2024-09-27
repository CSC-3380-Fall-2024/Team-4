package org.eru.errorhandling.exceptions.eru;

import org.eru.errorhandling.BaseException;

public class InvalidPlatformException extends BaseException {
    public InvalidPlatformException(String platform) {
        super("errors.org.eru.invalid_platform", "Invalid platform: '" + platform + "'", 400, 16104);
    }
}
