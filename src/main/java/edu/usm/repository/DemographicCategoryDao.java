package edu.usm.repository;

import edu.usm.domain.DemographicCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Repository for {@link DemographicCategory}
 */
@Repository
public interface DemographicCategoryDao extends CrudRepository<DemographicCategory, String>{

    /**
     * Returns all Demographic Categories
     * @return {@link java.util.Set} of {@link DemographicCategory}
     */
    @Override
    HashSet<DemographicCategory> findAll();

    /**
     * Finds a demographic category by its name if it exists.
     * @param name The name of the demographic category
     * @return {@link DemographicCategory}
     */
    DemographicCategory findByName(String name);

}
