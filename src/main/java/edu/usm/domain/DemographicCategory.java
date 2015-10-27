package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by andrew on 10/16/15.
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DemographicOption> getOptions() {
        return options;
    }

    public void setOptions(Set<DemographicOption> options) {
        this.options = options;
    }
}
