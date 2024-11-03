package org.eru.errorhandling.exceptions.eru;

import org.eru.errorhandling.BaseException;

public class NoOffersException  extends BaseException {
    public NoOffersException() {
        super("errors.com.epicgames.eru.empty_offers", "There are no line offers!", 400, 16202);
    }
}