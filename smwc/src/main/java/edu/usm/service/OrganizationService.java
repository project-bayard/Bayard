package edu.usm.service;

import edu.usm.domain.Organization;

import java.util.Set;

/**
 * Created by scottkimball on 4/11/15.
 */
public interface OrganizationService {

    Organization findById (String id);
    Set<Organization> findAll();
    void delete (Organization organization);
    void update (Organization organization);
    String create(Organization organization);
    void deleteAll();
}
