package org.eru.errorhandling.exceptions.account;

import org.eru.errorhandling.BaseException;

public class PasswordEmptyException extends BaseException {
    public PasswordEmptyException() {
        super("errors.org.eru.account.password_empty", "Accounts require a password. Submit one.", 400, 18034);
    }
}
