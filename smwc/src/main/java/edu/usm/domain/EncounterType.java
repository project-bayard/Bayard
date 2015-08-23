package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by scottkimball on 4/15/15.
 */

@Entity(name = "encounter_type")
public class EncounterType extends BasicEntity implements Serializable {



    @Column(nullable = false, unique = true)
    @JsonView({Views.ContactDetails.class, Views.ContactEncounterDetails.class})
    private String name;

    public EncounterType(String name) {
        this.name = name;
    }

    public EncounterType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
