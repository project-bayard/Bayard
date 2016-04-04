package edu.usm.repository;

import edu.usm.domain.Committee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Repository for {@link Committee}
 */

@Repository
public interface CommitteeDao extends CrudRepository<Committee, String> {

    /**
     * Returns a Set of all Committees
     * @return An {@link java.util.Set} of {@link Committee}
     */
    @Override
    HashSet<Committee> findAll();

    /**
     * Finds a Committee by its name, if it exists
     * @param name The name of the committee
     * @return {@link Committee}
     */
    Committee findByName(String name);
}
