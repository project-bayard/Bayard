package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * An option of a DemographicCategory. Represents a possible valid response from a Contact.
 */
@Entity
public class DemographicOption extends BasicEntity {

    @Column
    @NotNull
    @JsonView({Views.DemographicDetails.class})
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private DemographicCategory category;

    public DemographicOption() {
        super();
    }

    /**
     * @param optionName the name of the option
     */
    public DemographicOption(String optionName) {
        this.name = optionName;
    }

    /**
     * @return the name of the option
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the DemographicCategory to which this option applies
     */
    public DemographicCategory getCategory() {
        return category;
    }

    public void setCategory(DemographicCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DemographicOption option = (DemographicOption) o;

        if (!name.equals(option.name)) return false;
        return !(category != null ? !category.equals(option.category) : option.category != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
