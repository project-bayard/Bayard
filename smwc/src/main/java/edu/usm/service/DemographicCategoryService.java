package edu.usm.service;

import edu.usm.domain.DemographicCategory;
import edu.usm.domain.DemographicOption;

import java.util.Set;

/**
 * Created by andrew on 10/16/15.
 */
public interface DemographicCategoryService {

    String create(DemographicCategory demographicCategory);
    void addOption(DemographicOption option, DemographicCategory category);
    DemographicCategory findById(String id);
    DemographicCategory findByName(String categoryName);
    void update(DemographicCategory demographicCategory);
    void delete(DemographicCategory demographicCategory);
    Set<DemographicCategory> findAll();
    void deleteAll();

}
