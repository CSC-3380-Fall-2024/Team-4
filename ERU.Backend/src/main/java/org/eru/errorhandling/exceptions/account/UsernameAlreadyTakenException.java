package org.eru.errorhandling.exceptions.account;

import org.eru.errorhandling.BaseException;

public class UsernameAlreadyTakenException extends BaseException {
    public UsernameAlreadyTakenException() {
        super("errors.org.eru.account.username_already_taken", "Your username has already been taken. Please choose another one.", 400, 18033);
    }
}
