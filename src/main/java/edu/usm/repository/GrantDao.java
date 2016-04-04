package edu.usm.repository;

import edu.usm.domain.Grant;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Repository for Grants
 */
public interface GrantDao extends CrudRepository<Grant, String>{

    /**
     * Returns all existing grants.
     * @return {@link java.util.Set} of {@link Grant}
     */
    @Override
    HashSet<Grant> findAll();

}
