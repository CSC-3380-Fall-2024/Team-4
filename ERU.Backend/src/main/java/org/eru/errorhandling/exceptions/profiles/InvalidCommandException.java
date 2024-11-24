package org.eru.errorhandling.exceptions.profiles;

import org.eru.errorhandling.BaseException;

import java.util.Arrays;

public class InvalidCommandException extends BaseException {
    public InvalidCommandException(String command, String[] profiles) {
        super("errors.org.eru.modules.profiles.invalid_command", command + " is not valid on " + profiles.length + " profiles (" + Arrays.toString(profiles) + ")", 400, 12801);
    }
}
