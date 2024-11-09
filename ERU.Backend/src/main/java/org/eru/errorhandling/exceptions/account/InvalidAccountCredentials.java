package org.eru.errorhandling.exceptions.account;

import org.eru.errorhandling.BaseException;

public class InvalidAccountCredentials extends BaseException {
    public InvalidAccountCredentials() {
        super("errors.com.epicgames.account.invalid_account_credentials", "Sorry the account credentials you are using are invalid", 400, 18031);
    }
}
