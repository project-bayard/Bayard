package edu.usm.dto;

/**
 * Created by scottkimball on 6/19/15.
 */
public class Response {

    public static final String SUCCESS = "SUCCESS";
    public static final String TYPE_CONSTRAINT_VIOLATION = "Constraint Violation";
    public static final String TYPE_NULL_DOMAIN_REFERENCE = "Null Reference";
    public static final String TYPE_ACCESS_DENIED = "Access Denied";
    private static final String TYPE_NOT_PROVIDED = "N/A";

    private String id;
    private String message;
    private String type;

    private static final Response GENERIC = new Response(null, SUCCESS);

    public static Response successGeneric() {
        return GENERIC;
    }

    public Response(String id, String message, String type) {
        this.id = id;
        this.message = message;
        this.type = type;
    }

    public Response(String id, String message) {
        this.id = id;
        this.message = message;
        this.type = TYPE_NOT_PROVIDED;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
