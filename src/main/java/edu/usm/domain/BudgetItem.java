package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Entity that models a budget item for entities in the development domain.
 */
@Entity
public class BudgetItem extends BasicEntity implements Serializable {

    @Column(nullable = false, unique = true)
    @JsonView(Views.DonationDetails.class)
    private String name;

    public BudgetItem() {
        super();
    }

    /**
     * @param name The name of the budget item
     */
    public BudgetItem(String name) {
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
