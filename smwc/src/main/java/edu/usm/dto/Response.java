package edu.usm.dto;

/**
 * Created by scottkimball on 6/19/15.
 */
public class Response {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    private String id;
    private String status;
    private String message;

    private static final Response GENERIC = new Response(null, Response.SUCCESS, null);
    private static final Response UNAUTHORIZED_USER = new Response(null, FAILURE, "You are not authorized to perform this action");

    public static Response failNonexistentContact(String id) {
        return new Response(null,Response.FAILURE, "Contact with ID " + id + " does not exist");
    }

    public static Response successGeneric() {
        return GENERIC;
    }

    public static Response unauthorized() {
        return UNAUTHORIZED_USER;
    }

    public Response(String id, String status, String message) {
        this.id = id;
        this.status = status;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
