package org.eru.errorhandling.exceptions.common;

import org.eru.errorhandling.BaseException;

public class ServerErrorException extends BaseException {
    public ServerErrorException() {
        super("errors.com.epicgames.common.server_error", "Sorry the server encountered an error processing your request.", 500, 1000);
    }
}
