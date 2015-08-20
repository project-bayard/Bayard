package edu.usm.dto;

/**
 * Created by scottkimball on 6/19/15.
 */
public class Response {

    public static final String SUCCESS = "SUCCESS";

    private String id;
    private String message;

    private static final Response GENERIC = new Response(null, SUCCESS);

    public static Response successGeneric() {
        return GENERIC;
    }

    public Response(String id, String message) {
        this.id = id;
        this.message = message;
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
}
