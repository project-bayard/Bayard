package edu.usm.service;

import edu.usm.domain.Organization;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by scottkimball on 4/11/15.
 */
public interface OrganizationService {

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    Organization findById (String id);

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    Set<Organization> findAll();

    @PreAuthorize(value = "hasRole('ROLE_ELEVATED')")
    void delete (Organization organization);

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    void update (Organization organization);

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    String create(Organization organization);

    @PreAuthorize(value = "hasRole('ROLE_SUPERUSER')")
    void deleteAll();
}
