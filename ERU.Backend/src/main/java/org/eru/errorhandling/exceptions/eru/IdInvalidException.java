package org.eru.errorhandling.exceptions.eru;

import org.eru.errorhandling.BaseException;

public class IdInvalidException extends BaseException {
    public IdInvalidException(String offerId) {
        super("errors.com.epicgames.eru.id_invalid", "Offer ID (" + offerId + ") not found", 400, 1040);
    }
}
