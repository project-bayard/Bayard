package edu.usm.repository;

import edu.usm.domain.Foundation;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Repository for {@link Foundation}
 */
public interface FoundationDao extends CrudRepository<Foundation, String> {

    /**
     * Returns all existing foundations.
     * @return {@link java.util.Set} of {@link Foundation}
     */
    @Override
    HashSet<Foundation> findAll();

    /**
     * Finds a foundation by its name, if it exists.
     * @param name
     * @return {@link Foundation}
     */
    Foundation findByName(String name);

}
