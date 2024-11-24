package org.eru.errorhandling.exceptions.eru;

import org.eru.errorhandling.BaseException;

public class InvalidPartyPlayerIdsException extends BaseException {
    public InvalidPartyPlayerIdsException() {
        super("errors.org.eru.eru.party.invalid_party_user_ids", "blank partyUserIds", 400, 16103);
    }
}
