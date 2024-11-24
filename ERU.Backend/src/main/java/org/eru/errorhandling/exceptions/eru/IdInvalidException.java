package org.eru.errorhandling.exceptions.eru;

import org.eru.errorhandling.BaseException;

public class IdInvalidException extends BaseException {
    public IdInvalidException(String offerId) {
        super("errors.org.eru.eru.id_invalid", "Offer ID (" + offerId + ") not found", 400, 1040);
    }
}
