package org.eru.errorhandling.exceptions.profiles;

import org.eru.errorhandling.BaseException;

public class OperationForbiddenException extends BaseException {
    public OperationForbiddenException(String accoundId) {
        super("errors.com.epicgames.modules.profiles.operation_forbidden", "Unable to find template configuration for profile " + accoundId, 200, 12813);
    }
}
