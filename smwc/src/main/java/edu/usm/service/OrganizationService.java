package edu.usm.service;

import edu.usm.domain.Organization;

import java.util.List;

/**
 * Created by scottkimball on 4/11/15.
 */
public interface OrganizationService {

    Organization findById (String id);
    List<Organization> findAll();
    void delete (Organization organization);
    void update (Organization organization);
    void create(Organization organization);
    void deleteAll();
}
