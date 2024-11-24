package org.eru.errorhandling.exceptions.eru;

import org.eru.errorhandling.BaseException;

public class OperationNotFoundException extends BaseException {
    public OperationNotFoundException(String operation) {
        super("errors.org.eru.eru.operation_not_found", "Operation " + operation + " not valid", 404, 16035);
    }
}
