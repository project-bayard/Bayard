package edu.usm.repository;

import edu.usm.domain.Encounter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.SortedSet;

/**
 * Repository for Encounters
 */

@Repository
public interface EncounterDao extends CrudRepository<Encounter, String> {

    /**
     * Returns all existing encounters
     * @return {@link java.util.Set} of {@link Encounter}
     */
    @Override
    SortedSet<Encounter> findAll();
}
