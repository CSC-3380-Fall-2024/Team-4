package org.eru.errorhandling.exceptions.account;

import org.eru.errorhandling.BaseException;

public class AccountNotActiveException extends BaseException {
    public AccountNotActiveException() {
        super("errors.org.eru.account.account_not_active", "You have been permanently banned from ERU", 400, -1);
    }
}
