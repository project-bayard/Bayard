package edu.usm.domain.exception;

/**
 * Created by scottkimball on 2/6/16.
 */
public class NotFoundException extends Exception {
    private static final String DEFAULT_MESSAGE = "Contact with those details does not exist";

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException createException() {
        return new NotFoundException(DEFAULT_MESSAGE);
    }
}
