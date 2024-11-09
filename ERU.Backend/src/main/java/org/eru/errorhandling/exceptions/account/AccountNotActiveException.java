package org.eru.errorhandling.exceptions.account;

import org.eru.errorhandling.BaseException;

public class AccountNotActiveException extends BaseException {
    public AccountNotActiveException() {
        super("errors.com.epicgames.account.account_not_active", "Sorry, the account you are using is not active", 400, 18006);
    }
}
