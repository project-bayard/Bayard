package edu.usm.repository;

import edu.usm.domain.EncounterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Repository for EncounterType
 */

@Repository
public interface EncounterTypeDao extends CrudRepository<EncounterType, String> {

    /**
     * Finds an EncounterType by its name, if it exists.
     * @param name The name of the EncounterType
     * @return {@link EncounterType}
     */
    EncounterType findByName(String name);

    /**
     * Returns all existing EncounterTypes
     * @return {@link java.util.Set} of {@link EncounterType}
     */
    @Override
    HashSet<EncounterType> findAll();
}
