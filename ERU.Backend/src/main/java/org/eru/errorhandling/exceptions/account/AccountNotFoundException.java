package org.eru.errorhandling.exceptions.account;

import org.eru.errorhandling.BaseException;

public class AccountNotFoundException extends BaseException {
    public AccountNotFoundException(String accountId) {
        super("errors.org.eru.account.account_not_found", "Unable to find your account \"" + accountId + "\". Please try again later.", 404, -1);
    }
}
