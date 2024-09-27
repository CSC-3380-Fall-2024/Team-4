package org.eru.errorhandling.exceptions.common;

import org.eru.errorhandling.BaseException;

public class ThrottledException extends BaseException {
    public ThrottledException() {
        super("errors.org.eru.common.throttled", "You are being throttled. Please try again later.", 429, 1041);
    }
}
