package org.eru.errorhandling.exceptions.account;

import org.eru.errorhandling.BaseException;

public class EmailAlreadyTakenException extends BaseException {
    public EmailAlreadyTakenException() {
        super("errors.org.eru.account.email_already_taken", "Your e-mail is already registered to an account. Please check it and try again.", 400, 18032);
    }
}
