package org.eru.errorhandling.exceptions.account;

import org.eru.errorhandling.BaseException;

public class InvalidAccountCredentials extends BaseException {
    public InvalidAccountCredentials() {
        super("errors.org.eru.account.invalid_account_credentials", "Your e-mail and/or password is incorrect. Please check them and try again.", 400, 18031);
    }
}
