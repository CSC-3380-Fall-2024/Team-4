package org.eru.errorhandling.exceptions.cloudstorage;

import org.eru.errorhandling.BaseException;

public class FileNotFoundException extends BaseException {
    public FileNotFoundException(String file, String accountId) {
        super("errors.org.eru.cloudstorage.file_not_found", "Sorry, we couldn't find a file " + file + " for account " + accountId, 404, 12007);
    }
}
