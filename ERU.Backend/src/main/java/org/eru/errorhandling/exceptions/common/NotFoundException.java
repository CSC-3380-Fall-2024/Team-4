package org.eru.errorhandling.exceptions.common;

import org.eru.errorhandling.BaseException;

public class NotFoundException extends BaseException {
    public NotFoundException() {
        super("errors.org.eru.common.not_found", "Sorry the resource you were trying to find could not be found.", 404, 1004);
    }
}
