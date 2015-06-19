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

    public Response(String id, String status) {
        this.id = id;
        this.status = status;
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
