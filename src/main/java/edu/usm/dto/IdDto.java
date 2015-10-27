package edu.usm.dto;

import java.io.Serializable;

/**
 * Created by Andrew on 6/9/2015.
 */
public class IdDto implements Serializable{

    private static final long serialVersionUID = 1L;

    public String id;

    public IdDto(){};

    public IdDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
