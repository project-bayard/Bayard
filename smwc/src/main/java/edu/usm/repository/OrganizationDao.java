package edu.usm.repository;

import edu.usm.domain.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by scottkimball on 2/22/15.
 */

@Repository
public interface OrganizationDao extends CrudRepository<Organization, String> {
}
