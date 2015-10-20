package edu.usm.repository;

import edu.usm.domain.DemographicCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Created by andrew on 10/16/15.
 */
@Repository
public interface DemographicCategoryDao extends CrudRepository<DemographicCategory, String>{

    @Override
    HashSet<DemographicCategory> findAll();

    DemographicCategory findByName(String name);

}
