package org.eru.errorhandling.exceptions.eru;

import org.eru.errorhandling.BaseException;

public class InvalidBucketIdException extends BaseException {
    public InvalidBucketIdException() {
        super("errors.org.eru.eru.invalid_bucket_id", "blank bucketId", 400, 16102);
    }
}
