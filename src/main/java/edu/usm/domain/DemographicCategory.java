package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A demographic qualifier of a Contact for which there may be several valid DemographicOptions
 */
@Entity
public class DemographicCategory extends BasicEntity implements Serializable{

    public static final String CATEGORY_RACE = "Race";
    public static final String CATEGORY_ETHNICITY = "Ethnicity";
    public static final String CATEGORY_GENDER = "Gender";
    public static final String CATEGORY_SEXUAL_ORIENTATION = "Sexual Orientation";
    public static final String CATEGORY_INCOME_BRACKET = "Income Bracket";

    @Column(unique = true)
    @NotNull
    @JsonView({Views.DemographicDetails.class})
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonView({Views.DemographicDetails.class})
    private Set<DemographicOption> options = new HashSet<>();

    public DemographicCategory() {
        super();
    }

    /**
     * @param categoryName the name of the category
     */
    public DemographicCategory(String categoryName) {
        super();
        this.name = categoryName;
    }

    /**
     * @return the category's name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the valid options for this category
     */
    public Set<DemographicOption> getOptions() {
        return options;
    }

    public void setOptions(Set<DemographicOption> options) {
        this.options = options;
    }
}
