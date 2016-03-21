package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Models a type of interaction between a person and a known Foundation
 */
@Entity
public class InteractionRecordType extends BasicEntity implements Serializable {

    @Column(nullable = false, unique = true)
    @JsonView({Views.InteractionRecordDetails.class, Views.InteractionRecordList.class})
    private String name;

    public InteractionRecordType() {
        super();
    }

    /**
     *
     * @param name The name of this type of interaction record
     */
    public InteractionRecordType(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
