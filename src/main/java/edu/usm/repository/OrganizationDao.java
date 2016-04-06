package edu.usm.repository;

import edu.usm.domain.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Repository for Organizations
 */

@Repository
public interface OrganizationDao extends CrudRepository<Organization, String> {

    /**
     * Returns all existing organizations.
     * @return {@link java.util.Set} of {@link Organization}
     */
    @Override
    HashSet<Organization> findAll();

    /**
     * Finds an organization by its name, if it exists.
     * @param name
     * @return {@link Organization}
     */
    Organization findOneByName(String name);

    Organization findByDonations_id(String id);
}
