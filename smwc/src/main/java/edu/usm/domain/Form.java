package edu.usm.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by scottkimball on 2/19/15.
 */
@Entity(name = "form")
public class Form extends BasicEntity  implements Serializable {

    @OneToOne
    private Encounter encounter;

    @Column
    private String name;

    @Column
    private String entry;

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
