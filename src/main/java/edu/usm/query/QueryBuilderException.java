package edu.usm.query;

/**
 * Created by scottkimball on 5/19/16.
 */
public class QueryBuilderException extends Exception {

    public QueryBuilderException(String message) {
        super(message);
    }

    public QueryBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
