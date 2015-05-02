package edu.usm.repository;

import edu.usm.domain.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Created by scottkimball on 2/22/15.
 */

@Repository
public interface OrganizationDao extends CrudRepository<Organization, String> {

    @Override
    HashSet<Organization> findAll();
}
